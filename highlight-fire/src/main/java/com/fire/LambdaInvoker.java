package com.fire;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class LambdaInvoker {

    AWSLambda lambdaClient;
    BasicAWSCredentials awsCreds = new BasicAWSCredentials("<hidden-access-key>", "<hidden-secret-key>");

    public LambdaInvoker() {
        // Initialize the AWS Lambda client
        lambdaClient = AWSLambdaClientBuilder
            .standard()
            .withRegion(Regions.US_AEAST_2)
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
    }

    public String invokeLambda(String imageStr, float threshold) {

        // Replace with your Lambda function name
        String functionName = "arn:aws:lambda:us-east-2:<hidden-account-id>:function:highlight_fire";

        // Create an input payload as a JSON string
        String inputPayload = String.format("{\"imageStr\": \"%s\", \"threshold\": \"%.2f\"}", imageStr, threshold);

        // Create an InvokeRequest
        InvokeRequest invokeRequest = new InvokeRequest()
            .withFunctionName(functionName)
            .withPayload(inputPayload);

        // Invoke the Lambda function
        InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);

        // Process the result
        byte[] functionOutput = invokeResult.getPayload().array();
        String functionOutputString = new String(functionOutput);
        // remove leading and trailing extra double quotes
        functionOutputString = functionOutputString.replaceAll("^\"|\"$", "");

        // Handle the function output as needed
        System.out.println("Lambda Function status code: " + invokeResult.getStatusCode());

        // Shutdown the Lambda client when done
        lambdaClient.shutdown();

        return functionOutputString;
    }
}
