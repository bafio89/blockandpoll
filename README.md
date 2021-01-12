DEPLOYARE su HEROKU

(installare plugin nel caso il comando deploy non sia riconosciuto : heroku plugins:install java)
usare comando: heroku deploy:jar target/blockandpoll-0.0.1-SNAPSHOT.jar --app pollalgorand

aggiunto system.properties  con proprietà java.runtime.version=11, per dire a heroku
 di usare la jdk 11 

link utile di algorand per callableTransaction https://developer.algorand.org/docs/features/asc1/stateful/sdks/


STARTARE L'app in locale

per startare il backend: play da intellj
per il frontend mettersi nel path .../pollgorand/src/main/frontend e lanciare comando npm start 
accedere da (localhost:9091)


Algoritmo per calcolo date.

- data 
- tempo = data - data attuale (in secondi)
- n. blocchi = tempo / block time algorand (tempo medio generazione blocchi)
- blocco attuale = leggi blocco attuale della Block chain.
- blocco scelto = blocco attuale + n.blocchi.



https://developer.algorand.org/tutorials/asa-java/#part-3-opt-in