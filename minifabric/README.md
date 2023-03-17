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

`minifab invoke -p '"GetAllAssets"'`

![image](https://user-images.githubusercontent.com/94653085/225779890-afeaa088-e0b5-4746-8e6d-aa59865811a1.png)

![image](https://user-images.githubusercontent.com/94653085/225780862-379a447c-7c4c-4247-9232-f46356d965f4.png)

![image](https://user-images.githubusercontent.com/94653085/225781345-7b8b3805-18b7-4bc8-b1bc-558a51aa4a9a.png)

![image](https://user-images.githubusercontent.com/94653085/225781500-9f3b1483-f523-4d32-a16d-d7bb1f996c3a.png)

![image](https://user-images.githubusercontent.com/94653085/225781684-2aec8075-b3e3-44cf-914b-9d4e612bf005.png)

![image](https://user-images.githubusercontent.com/94653085/225781899-78b8ad7e-afae-4d5d-8825-090b5a30deca.png)




