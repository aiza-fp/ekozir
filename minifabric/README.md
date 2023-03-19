# Minifabric erabiliz sarea abiaraztea

## Instalazioa

Minifabric instalazioa: https://github.com/litong01/minifabric

**spec.yaml** fitxategia instalazio karpetara kopiatu eta abiarazi komando hau erabiliz:

`minifab netup -i 2.4 -o org1.ekozir.com`

![image](https://user-images.githubusercontent.com/94653085/226176962-a8320f8d-6421-46dc-b5cf-de3d6b43d078.png)

![image](https://user-images.githubusercontent.com/94653085/226177087-a61addfa-38cd-4dac-8400-06efda29d04b.png)

## Kanal sortu:

`minifab create` (-c channel1) kanala zehazteko baina gero approve egitean ez dabil ondo, ez duela aurkitzen dio.

![image](https://user-images.githubusercontent.com/94653085/226177500-4328a2a5-6198-49af-bc00-727409269192.png)

## Profilak sortu:

`minifab profilegen`

## Chaincode bat instalatu

Instalazio karpetan vars/chaincode barrura joan, bertan ekozir/java karpeta sortu eta ChaincodeJava karpetako edukia jarri.

`minifab ccup -n ekozir -d false -l java`

edo
Install:

`minifab install -n ekozir -l java`

Approve and Commit:

`minifab approve,commit -n ekozir -d false -l java`

![image](https://user-images.githubusercontent.com/94653085/225779643-c97d04bb-1270-42d3-a4ea-5edbd9929829.png)

Discover:

`minifab discover`

![image](https://user-images.githubusercontent.com/94653085/225779994-b5a40574-ace4-4d46-9e7e-357cf1681edd.png)

Orain Chaincodeko funtzioak deitu ditzakegu:

`minifab invoke -p '"GetAllAssets"'`

![image](https://user-images.githubusercontent.com/94653085/225779890-afeaa088-e0b5-4746-8e6d-aa59865811a1.png)

![image](https://user-images.githubusercontent.com/94653085/225780862-379a447c-7c4c-4247-9232-f46356d965f4.png)

![image](https://user-images.githubusercontent.com/94653085/225781345-7b8b3805-18b7-4bc8-b1bc-558a51aa4a9a.png)

![image](https://user-images.githubusercontent.com/94653085/225781500-9f3b1483-f523-4d32-a16d-d7bb1f996c3a.png)

![image](https://user-images.githubusercontent.com/94653085/225781684-2aec8075-b3e3-44cf-914b-9d4e612bf005.png)

![image](https://user-images.githubusercontent.com/94653085/225781899-78b8ad7e-afae-4d5d-8825-090b5a30deca.png)

## Chaincode bertsio berri bat instalatu

´minifab ccup -n ekozir -l java -v 2.0´

## Explorer

Sarearen jokaera bistaratzeko 'explorer' bat instala eta exekutatu daiteke:

`minifab explorerup`

![image](https://user-images.githubusercontent.com/94653085/226072016-7614d23e-a390-4b68-9d52-3fb7553e1840.png)

![image](https://user-images.githubusercontent.com/94653085/226072192-7a14afdc-5969-4f23-8d50-db6ac69d3c34.png)





