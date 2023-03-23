# 3 organizazio dauzkan sarea sortzen Minifabric bidez

## 1.- Zornotzan

spec.yaml_fpzornotza erabiliz, hemen **vars/chaincode/ekozir/java** karpetan chaincodea jarriko dugu eta **up**-ekin dena abiarazi:

`minifab up -i 2.4 -o org.fpzornotza.com -c kanala -n ekozir -d false -l java -e 7000`

Abiarazi ostean `minifab channelquery`exekutatu **./vars/kanala_config.json** fitxategia sortzeko.

## 2.- Recymet-en

spec.yaml_recymet erabiliz, sarea bakarrik abiarazten dugu, **netup** erabiliz:

`minifab netup -i 2.4 -o org.recymet.com -e 7000`

Zornotzako makinako **vars/profiles/endpoints.yaml** fitxategia Recymet-eko **vars** karpetara kopiatu eta:

`minifab nodeimport` exekutatu Recymet makinan, jakin dezan orderer bat kanpoan dagoela.

## 3.- Zornotzan

Recymet makinako **./vars/JoinRequest_org-recymet-com.json** fitxategiaren edukia, lehen sortu dugun **kanala_config.json** fitxategian txertatu behar dugu:

![image](https://user-images.githubusercontent.com/94653085/227046176-d69c9e17-5073-43c2-a53a-6b5cf8775f00.png)

Bertsioa aldatu dela ere adierazi:

![image](https://user-images.githubusercontent.com/94653085/227047747-e42d7f7f-9a7c-4a67-9ace-06b44a57d20f.png)

`minifab channelsign,channelupdate` exekutatu kanaleko konfigurazio aldaketak eguneratzeko.

## 4.- Recymet-en

`minifab install -n ekozir -l java` (chaincodea vars/chaincode/ekozir/java karpetan utzi ostean).

`minifab join -c kanala`

`minifab approve`

`minifab anchorupdate`

`minifab profilegen`

## 5.- Zornotzan

`minifab approve`



