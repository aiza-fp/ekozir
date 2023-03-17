# Minifabric erabiliz sarea abiaraztea

Minifabric instalazioa: https://github.com/litong01/minifabric

**spec.yaml** fitxategia instalazio karpetara kopiatu eta abiarazi komando hau erabiliz:

`minifabric up -o org1.ekozir.com`

Instalazio karpetan vars/chaincode barrura joan, bertan ekozir/java karpeta sortu eta ChaincodeJava karpetako edukia jarri.

Install:

`minifab install -n ekozir -l java`

Approve and Commit:

`minifab approve,commit -n ekozir -d false -l java`

![image](https://user-images.githubusercontent.com/94653085/225779643-c97d04bb-1270-42d3-a4ea-5edbd9929829.png)

Discover:

`minifab discover`

![image](https://user-images.githubusercontent.com/94653085/225779994-b5a40574-ace4-4d46-9e7e-357cf1681edd.png)

Orain Chaincodeko funtzioak deitu ditzakegu:

´minifab invoke -p '"GetAllAssets"'

![image](https://user-images.githubusercontent.com/94653085/225779890-afeaa088-e0b5-4746-8e6d-aa59865811a1.png)


