[app]
app1 ansible_host=${app_ip} ansible_user=ubuntu ansible_ssh_private_key_file=~/dev/certificates/${key_name}.pem

[app:vars]
db_username=${database_username}
db_password=sifra123
db_host=${database_host}
db_name=${database_name}
db_resource=BTDB
tomcat_memory=1536M
ansible_python_interpreter=/usr/bin/python3
app_name=${project_name}
war_path=../../app/target/ROOT.war