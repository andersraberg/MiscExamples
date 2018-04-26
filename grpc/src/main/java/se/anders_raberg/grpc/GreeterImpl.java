package se.anders_raberg.grpc;

import io.grpc.stub.StreamObserver;
import se.anders_raberg.grpc.generated.GreeterGrpc;
import se.anders_raberg.grpc.generated.HelloRequest;
import se.anders_raberg.grpc.generated.HelloResponse;

class GreeterImpl extends GreeterGrpc.GreeterImplBase {
	
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse response = HelloResponse.newBuilder()
        		.setMessage("Hello " + request.getName())
        		.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}