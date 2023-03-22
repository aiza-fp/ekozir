# 3 organizazio dauzkan sarea sortzen Minifabric bidez

## 1.- Zornotzan

spec.yaml_fpzornotza erabiliz, hemen **vars/chaincode/ekozir/java** karpetan chaincodea jarriko dugu eta **up**-ekin dena abiarazi:

`minifab up -i 2.4 -o org.fpzornotza.com -c kanala -n ekozir -d false -l java -e 7000`

Abiarazi ostean `minifab channelquery`exekutatu **./vars/kanala_config.json** fitxategia sortzeko.

## 2.- Recymet-en

spec.yaml_recymet erabiliz, sarea bakarrik abiarazten dugu, **netup** erabiliz:

`minifab netup -o org.recymet.com -e 7000`

Zornotzako makinako **vars/profiles/endpoints.yaml** fitxategia Recymet-eko **vars** karpetara kopiatu eta:

`minifab nodeimport` exekutatu Recymet makinan, jakin dezan orderer bat kanpoan dagoela.

## 3.- Zornotzan

Berdina egingo dugu Recymet-eko orderer-a ezaguna izan dadin:

Recymet makinako **vars/profiles/endpoints.yaml** fitxategia Zornotzako **vars** karpetara kopiatu eta:

`minifab nodeimport` exekutatu Zornotzako makinan, jakin dezan orderer bat kanpoan dagoela.

`minifab profilegen` exekutatu dezakegu *vars/profiles/endpoints.yaml* eguneratzeko (makina bietan).

