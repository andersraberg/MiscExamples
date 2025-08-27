
keytool -genkeypair -alias myserver -keyalg RSA -keysize 2048 \
  -keystore server.keystore -storepass changeit \
  -validity 365 -dname "CN=localhost"


keytool -export -alias myserver -keystore server.keystore \
  -file server.crt -storepass changeit


keytool -importkeystore \
  -srckeystore server.keystore -srcstoretype JKS -srcstorepass changeit \
  -destkeystore server.p12 -deststoretype PKCS12 -deststorepass changeit
