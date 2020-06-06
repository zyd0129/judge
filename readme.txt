一、添加环境变量
CONFIG_SERVER=192.168.40.150:8848;CONFIG_SERVER_NAMESPACE=b90363b4-1d0e-4cbb-9410-a9b4e8d58e16;CONFIG_ACTIVE=dev

keytool -genkeypair -alias ps -keyalg RSA -keypass admin123 -keystore xc.keystore -storepass admin123

keytool -list -rfc --keystore xc.keystore | openssl x509 -inform pem -pubkey