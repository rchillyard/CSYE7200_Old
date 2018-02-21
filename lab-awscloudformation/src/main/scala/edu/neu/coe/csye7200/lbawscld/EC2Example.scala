package edu.neu.coe.csye7200.lbawscld

import com.monsanto.arch.cloudformation.model._
import com.monsanto.arch.cloudformation.model.resource._
import com.monsanto.arch.cloudformation.model.simple.Builders._

/**
  * aws cloudformation create-stack --stack-name "csye7200lab8" --template-body file://ec2.json --parameters ParameterKey=KeyName,ParameterValue=csye6225 ParameterKey=AllowSSHFrom,ParameterValue=0.0.0.0/0 ParameterKey=FolderName,ParameterValue=helloworld
  */
object EC2Example extends VPCWriter with App {

  val keyNameParameter = `AWS::EC2::KeyPair::KeyName_Parameter`(
    name = "KeyName",
    Description = Some("Name of an existing EC2 KeyPair to enable SSH access to the instances"),
    ConstraintDescription = Some("Value must be a valid AWS key pair name in your account.")
  )

  val allowSSHFromParameter = CidrBlockParameter(
    name = "AllowSSHFrom",
    Description = Some("The net block (CIDR) that SSH is available to.")
  )

  val folderName = StringParameter(
    name = "FolderName",
    Description = Some("Name of folder that will be created under /home/ubuntu.")
  )

  val parameters = Seq(keyNameParameter,allowSSHFromParameter,folderName)

  implicit val vpc = `AWS::EC2::VPC`("myVPC",CidrBlock(10, 0, 0, 0, 16),AmazonTag.fromName("myVPC"))

  implicit val subnet = `AWS::EC2::Subnet`("mySubnet",vpc,None,CidrBlock(10, 0, 0, 1, 24),AmazonTag.fromName("mySubnet"),Some(true))

  val (internetGatewayResource, gatewayToInternetResource) = withInternetGateway
  val publicRouteTable = withRouteTable("Public", 1)
  val publicRouteTableRoute = publicRouteTable.withRoute(
    visibility = "Public",
    routeTableOrdinal = 1,
    routeOrdinal = 1,
    connectionBobber = InternetGatewayRoute(ResourceRef(internetGatewayResource))
  )
  val publicSubnetRouteTableAss = withRouteTableAssoc("Public",1,publicRouteTable)
  val gatewayStuff = Template.fromResource(internetGatewayResource) ++
    publicRouteTable ++
    gatewayToInternetResource ++
    publicRouteTableRoute ++
    publicSubnetRouteTableAss

  val myEC2 = ec2("myEC2",
                  "t2.micro",
                  ParameterRef(keyNameParameter),
                  AMIId("ami-66506c1c"),
                  Seq(),
                  AmazonTag.fromName("myEC2") :+ AmazonTag("CodeDeploy","CodeDeployGroup"),
                  UserData = Some(`Fn::Base64`(
                    `Fn::Join`("",
                      Seq[Token[String]](
                        "#!/bin/bash -v\n",
                        "yum update -y --security\n",
                        "mkdir /home/ubuntu/",
                        ParameterRef(folderName),
                        "\n",
                        "# EOF\n"
                      )
                    )
                  )))

  val sshToMyEC2 = ParameterRef(allowSSHFromParameter) ->- 22 ->- myEC2

  val simpleTemplate = Template.fromSecurityGroupRoutable(myEC2) ++ Template.collapse(sshToMyEC2) ++ gatewayStuff ++ Template(
      AWSTemplateFormatVersion = "2010-09-09",
      Description = "Simple EC2 template",
      Parameters = Some(parameters),
      Conditions = None,
      Mappings = None,
      Resources = Seq(vpc,subnet),
      Outputs = None,
      Routables = None
    )
  writeStaxModule("ec2.json", simpleTemplate)

}
