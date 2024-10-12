Consideraciones
agregar tu url de conexion a mongodb
haga uso de la coleccion de postman que se deja solo reemplace ENDPOINT por la url del apigetway
por interno se deja expuesta la url apigateway para sus respectivas pruebas

Lambda Function:

Nombre: nequi-franquicia-lambda.
Handler: com.nequi.StreamLambdaHandler::handleRequest.
API Gateway:

Ruta {proxy+}: Permite capturar rutas dinámicas. Ahora, cualquier llamada a / o rutas adicionales será manejada por la Lambda.
Método ANY: Permite manejar múltiples métodos HTTP como GET, POST, PUT, etc.
S3 Bucket: Recuerda subir el archivo ZIP al bucket indicado en donde se indique en la plantilla de cloud formation:

bash
Copy code
aws s3 cp target/lambda.zip s3://my-bucket-name/path/to/your/
Deploy: Ejecuta el siguiente comando para desplegar:

bash
Copy code
aws cloudformation deploy --template-file franquicia-infra.yml --stack-name NequiLambdaStack --capabilities CAPABILITY_NAMED_IAM
API Gateway URL: La URL generada será:

arduino
https://<api-id>.execute-api.us-east-1.amazonaws.com/prod/{proxy+}
Con estos cambios, tu Lambda estará expuesta correctamente y lista para recibir cualquier tipo de petición HTTP. Si necesitas algo más, no dudes en avisarme.


