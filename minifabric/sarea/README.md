# 3 organizazio dauzkan sarea sortzen Minifabric bidez

## 1.- Zornotzan: sarea abiarazi, chaincodea instalatu

spec.yaml_fpzornotza erabiliz, hemen **vars/chaincode/ekozir/java** karpetan chaincodea jarriko dugu eta **up**-ekin dena abiarazi:

`minifab up -i 2.4 -o org.fpzornotza.com -c kanala -n ekozir -d false -l java -e 7000`

Abiarazi ostean `minifab channelquery`exekutatu **./vars/kanala_config.json** fitxategia sortzeko.

## 2.- Recymet-en: sarea abiarazi, Zornotzako orderer nodoak inportatu

spec.yaml_recymet erabiliz, sarea bakarrik abiarazten dugu, **netup** erabiliz:

`minifab netup -i 2.4 -o org.recymet.com -e 7000`

Zornotzako makinako **vars/profiles/endpoints.yaml** fitxategia Recymet-eko **vars** karpetara kopiatu eta:

`minifab nodeimport` exekutatu Recymet makinan, jakin dezan orderer bat kanpoan dagoela.

## 3.- Zornotzan: organizazio berria gehitu, kanalaren konfigurazioa eguneratu

Recymet makinako **./vars/JoinRequest_org-recymet-com.json** fitxategiaren edukia, lehen sortu dugun **kanala_config.json** fitxategian txertatu behar dugu:

Automatikoki egin daiteke fitxategi horren edukia **vars/NewOrgJoinRequest.json** bezala gordetzen badugu Zornotzan eta hau exekutatuz:

`minifab orgjoin` 

Komando honek egiten duena 'eskuz' ere egin daiteke:

![image](https://user-images.githubusercontent.com/94653085/227046176-d69c9e17-5073-43c2-a53a-6b5cf8775f00.png)

Bertsioa aldatu dela ere adierazi:

![image](https://user-images.githubusercontent.com/94653085/227047747-e42d7f7f-9a7c-4a67-9ace-06b44a57d20f.png)

Edozein kasutan, eskuz zein automatikoki egin, LifecycleEndorsement Policy aldatuko dugu edozein Admin-ek aldaketak egin ahal izateko, bestela beranduago arazoak ditugula ikusi dut:

![image](https://user-images.githubusercontent.com/94653085/227521369-fc8446e5-335c-4fc2-a22b-b5fea9c30f28.png)

`minifab channelsign,channelupdate` exekutatu kanaleko konfigurazio aldaketak eguneratzeko.

## 4.- Recymet-en

`minifab install -n ekozir -d false -l java` (chaincodea vars/chaincode/ekozir/java karpetan utzi ostean).

`minifab join -c kanala`

`minifab approve`

## 5.- Zornotzan

`minifab approve`

`minifab commit`

Orain Recymet makinan chaincodea duten kontainerrak martxan jarri direla ikusi dezakegu:

![image](https://user-images.githubusercontent.com/94653085/227524167-24e9fdd6-073d-4af7-9df5-8c7b007ef5b6.png)

Honekin Recymet eta Zornotzaren arteko sarea sortu dugu, bietako peer-ak chaincode berdina eta kanal berdinean daudelarik.

Orain makina batean zein bestean AppEkozir exekutatu dezakegu, identifikatu eta blockchain-ean erregistroak sortu/irakurri:

https://github.com/aiza-fp/ekozir/blob/master/ApplicationGatewayJava/src/main/java/AppEkozir.java

## Ormazabal gehitu

Prozedura berdina jarraituz Ormazabal gehitzeko, Zornotzan organizazio berria gehitzean errore hau ematen digu:

![image](https://user-images.githubusercontent.com/94653085/227774442-993e4456-7ef1-4feb-90ec-10b5c36956db.png)

Beste Admin batek ere onatu behar du aldaketa?
