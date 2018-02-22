package edu.neu.coe.csye7200.lbawscld

import com.monsanto.arch.cloudformation.model._
import com.monsanto.arch.cloudformation.model.resource._

/**
  * If you are not familiar with AWS and AWS CloudFormation, read following:
  * https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html
  * AWS CloudFormation Function Fn::Join:
  * https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-join.html
  */
object S3Bucket extends VPCWriter {

  //TODO add a String type parameter with name DomainName
  val domainParameter = ???

  //TODO add all parameters in a Seq
  val parameters = ???

  //TODO create a AWS::S3::Bucket with name testS3Bucket, BucketName csye7200.${DomainName}.lab, you can use AWS CloudFormation Function Fn::Join to achieve this
  val myS3Bucket = ???

  val simpleTemplate = Template(
    AWSTemplateFormatVersion = "2010-09-09",
    Description = "Simple S3 Bucket template",
    Parameters = parameters,
    Conditions = None,
    Mappings = None,
    Resources = Seq(myS3Bucket),
    Outputs = None,
    Routables = None
  )

  def main(args: Array[String]): Unit =  {
    writeStaxModule("s3.json", simpleTemplate)
  }

}
