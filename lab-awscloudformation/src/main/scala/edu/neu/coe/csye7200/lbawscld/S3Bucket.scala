package edu.neu.coe.csye7200.lbawscld

import com.monsanto.arch.cloudformation.model._
import com.monsanto.arch.cloudformation.model.resource._

object S3Bucket extends VPCWriter {

  val domainParameter = StringParameter(
    name = "DomainName"
  )

  val parameters = Seq(domainParameter)

  val myS3Bucket = `AWS::S3::Bucket`("testS3Bucket",Some(`Fn::Join`(".",Seq("csye7200",ParameterRef(domainParameter),"lab"))))

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
