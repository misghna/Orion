echo '********** starting Deployment to AWS server********'
cd orion-web

echo '********** Building Client Side ********'
ng build --prod
cd ../orion-server

echo '********** Building Server Side Code********'
mvn clean
mvn install

echo '********** Coping FIles  to AWS server********'
scp -i /Users/mgebreki/Downloads/ansebaKey.pem /Users/mgebreki/gitOrion/Orion/orion-server/target/ROOT.war ec2-user@54.93.52.249:/tmp

echo '********** Deploying app to server********'
ssh -i /Users/mgebreki/Downloads/ansebaKey.pem ec2-user@54.93.52.249 sudo mv /tmp/ROOT.war /usr/share/tomcat8/webapps/ROOT.war 
