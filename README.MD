### backup BD:
A documentação para fazer o backup do banco de dados fica em: https://blog.railway.app/p/postgre-backup
Basicamente, para fazer um backup do BD utilizamos um comando dentro do diretório desejado para o backup. Segue o comando: pg_dump -U postgres -h containers-us-west-54.railway.app -p 7262 -W -F t railway > mydatabaseappcasalbackup
Para restaurar, utilizamos o comando: pg_restore -U postgres -h containers-us-west-54.railway.app -p 7262 -W -F t -d railway mydatabaseappcasalbackup

Caso um dia queira fazer uma melhoria para salvar o bkp do BD na Amazon e automatizado, ao invés de um arquivo local, a documentação fica em: https://blog.railway.app/p/automated-postgresql-backups
