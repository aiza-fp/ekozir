# 3 organizazio dauzkan sarea sortzen Minifabric bidez 

Hiru makinetan **minifabric** instalatu:

`mkdir -p ~/fpzornotza && cd ~/fpzornotza && curl -o minifab -sL https://tinyurl.com/yxa2q6yr && chmod +x minifab`

`mkdir -p ~/recymet && cd ~/recymet && curl -o minifab -sL https://tinyurl.com/yxa2q6yr && chmod +x minifab`

`mkdir -p ~/ormazabal && cd ~/ormazabal && curl -o minifab -sL https://tinyurl.com/yxa2q6yr && chmod +x minifab`

## 1.- Zornotzan: sarea abiarazi, chaincodea instalatu

spec.yaml_fpzornotza erabiliz, hemen **vars/chaincode/ekozir/java** karpetan chaincodea jarriko dugu eta **up**-ekin dena abiarazi:

`minifab up -i 2.4 -o org.fpzornotza.com -c kanala -n ekozir -d false -l java -e 7000`

Abiarazi ostean `minifab channelquery`exekutatu **./vars/kanala_config.json** fitxategia sortzeko.

## 2.- Recymet eta Ormazabal-en: sarea abiarazi, Zornotzako orderer nodoak inportatu

spec.yaml_recymet eta spec.yaml_ormazabal erabiliz (baina spec.yaml izenarekin), sarea bakarrik abiarazten dugu, **netup** erabiliz:

`minifab netup -i 2.4 -o org.recymet.com -e 7000`
`minifab netup -i 2.4 -o org.ormazabal.com -e 7000`

Zornotzako makinako **vars/profiles/endpoints.yaml** fitxategia Recymet eta Ormazabal-eko **vars** karpetara kopiatu eta:

`minifab nodeimport` exekutatu bi makinetan, jakin dezaten orderer-ak Zornotzako makinan daudela.

## 3.- Zornotzan: kanalaren konfigurazioa eguneratu, organizazio berriak gehitu

Admins eta LifecycleEndorsement Policy aldatuko dugu edozein Admin-ek aldaketak egin ahal izateko, bestela beranduago arazoak ditugula ikusi dut:

![image](https://user-images.githubusercontent.com/94653085/227966728-e352492d-0c16-47aa-9515-8f5010c1be2c.png)

`minifab channelsign,channelupdate` exekutatu kanaleko konfigurazio aldaketak eguneratzeko.

Recymet makinako **./vars/JoinRequest_org-recymet-com.json** fitxategiaren edukia, lehen sortu dugun **kanala_config.json** fitxategian txertatu behar dugu:

Automatikoki egin daiteke fitxategi horren edukia **vars/NewOrgJoinRequest.json** bezala gordetzen badugu Zornotzan eta hau exekutatuz:

`minifab orgjoin`

Berdina egin baina orain Ormazabal-eko **./vars/JoinRequest_org-ormazabal-com.json** fitxategiarekin.

Komando honek egiten duena 'eskuz' ere egin daiteke (*orgjoin* erabili badugu ez da beharrezkoa):

![image](https://user-images.githubusercontent.com/94653085/227046176-d69c9e17-5073-43c2-a53a-6b5cf8775f00.png)

Bertsioa aldatu dela ere adierazi:

![image](https://user-images.githubusercontent.com/94653085/227047747-e42d7f7f-9a7c-4a67-9ace-06b44a57d20f.png)

## 4.- Recymet eta Ormazabal-en

`minifab install -n ekozir -d false -l java` (chaincodea vars/chaincode/ekozir/java karpetan utzi ostean).

`minifab join -c kanala`

`minifab approve`

## 5.- Zornotzan

`minifab approve`

`minifab commit`

Orain Recymet eta Ormazabal makinetan chaincodea duten kontainerrak martxan jarri direla ikusi dezakegu:

![image](https://user-images.githubusercontent.com/94653085/227524167-24e9fdd6-073d-4af7-9df5-8c7b007ef5b6.png)

Honekin Recymet, Ormazabal eta Zornotzaren arteko sarea sortu dugu, hiruetako peer-ak chaincode berdina dutelarik eta kanal berdinean daudelarik.

Orain edozein makinatan AppEkozir exekutatu dezakegu, identifikatu eta blockchain-ean erregistroak sortu/irakurri:

https://github.com/aiza-fp/ekozir/blob/master/ApplicationGatewayJava/src/main/java/AppEkozir.java

## Emaitzak

![image](https://user-images.githubusercontent.com/94653085/227968398-8fc655ea-33d4-4115-9667-a87f994d8165.png)

![image](https://user-images.githubusercontent.com/94653085/227968531-9c1259a0-0cf3-43dd-987f-7cf90e66765c.png)

![image](https://user-images.githubusercontent.com/94653085/227968589-fa78738b-b9de-40f6-8eeb-c7e5847a4846.png)

![image](https://user-images.githubusercontent.com/94653085/227968702-8dc15f0a-02af-4ea8-826d-7b43af16e268.png)

