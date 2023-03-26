# Minifabric erabiliz sarea abiaraztea

## Instalazioa

Minifabric instalazioa: https://github.com/litong01/minifabric

Datozen pauso guztiak komando baten bidez egin daitezke **vars/chaincode/ekozir/java** karpeta sortu eta bertan ChaincodeJava karpetaren edukia jartzen badugu:

`minifab up -i 2.4 -o org1.ekozir.com -c channel1 -n ekozir -d false -l java` (-e 7000) gehitu kontainerrak hostean eskuragarri izateko 7000 portutik aurrera. Honekin gero ApplicationGatewayJava erabiltzea posible izango da.

![image](https://user-images.githubusercontent.com/94653085/226212857-b68aaf08-879c-4db5-b9e3-ba1b83348efc.png)

## Sarea sortu:

**spec.yaml** fitxategia instalazio karpetara kopiatu eta abiarazi komando hau erabiliz:

`minifab netup -i 2.4 -o org1.ekozir.com`

![image](https://user-images.githubusercontent.com/94653085/226176962-a8320f8d-6421-46dc-b5cf-de3d6b43d078.png)

![image](https://user-images.githubusercontent.com/94653085/226177087-a61addfa-38cd-4dac-8400-06efda29d04b.png)

## Kanala sortu:

`minifab create -c channel1`

![image](https://user-images.githubusercontent.com/94653085/226177500-4328a2a5-6198-49af-bc00-727409269192.png)

## Kanalera batu:

`minifab join`

## Kanala eguneratu peer guztiak anchor motakoak izateko:

`minifab anchorupdate`

## Profilak sortu:

`minifab profilegen`

## Chaincode bat instalatu

Instalazio karpetan vars/chaincode barrura joan, bertan ekozir/java karpeta sortu eta ChaincodeJava karpetako edukia jarri.

`minifab ccup -n ekozir -d false -l java`

Honek pausu guzti hauek batera egiten ditu: install, approve, commit, initialize, discover

![image](https://user-images.githubusercontent.com/94653085/226213842-0e240982-c52c-4401-9f8d-347f2ccb2e4d.png)

Orain Chaincodeko funtzioak deitu ditzakegu:

`minifab invoke -p '"GetAllAssets"'`

![image](https://user-images.githubusercontent.com/94653085/225779890-afeaa088-e0b5-4746-8e6d-aa59865811a1.png)

![image](https://user-images.githubusercontent.com/94653085/225780862-379a447c-7c4c-4247-9232-f46356d965f4.png)

![image](https://user-images.githubusercontent.com/94653085/225781345-7b8b3805-18b7-4bc8-b1bc-558a51aa4a9a.png)

![image](https://user-images.githubusercontent.com/94653085/225781500-9f3b1483-f523-4d32-a16d-d7bb1f996c3a.png)

![image](https://user-images.githubusercontent.com/94653085/225781684-2aec8075-b3e3-44cf-914b-9d4e612bf005.png)

![image](https://user-images.githubusercontent.com/94653085/225781899-78b8ad7e-afae-4d5d-8825-090b5a30deca.png)

## Kanalaren konfigurazioa aldatu

Momentuko konfigurazioa lortzeko:

`minifab channelquery` --> honek configurazioaren .json fitxategi bat sortuko du vars karpetan (zein den esango digu).

Fitxategi horretako parametroak aldatu ditzakegu eta aldatu ostean:

`minifab channelsign`

`minifab channelupdate`

## Chaincode bertsio berri bat instalatu

`minifab ccup -n ekozir -l java -v 2.0`

## Explorer

Sarearen jokaera bistaratzeko 'explorer' bat instala eta exekutatu daiteke:

`minifab explorerup`

![image](https://user-images.githubusercontent.com/94653085/226072016-7614d23e-a390-4b68-9d52-3fb7553e1840.png)

![image](https://user-images.githubusercontent.com/94653085/226072192-7a14afdc-5969-4f23-8d50-db6ac69d3c34.png)




