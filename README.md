# Blockchain
A Java based model of a Blockchain that stores data for cryptocurrency transactions. 

## Model
 - is composed of blocks that **store** transaction data
 - implements a simple mining system
 - has a **digital signature** that chains blocks together
 - requires "proof of work" mining to validate new blocks
 - can be used to check if data in the blockchain is **valid and unchanged**
 - allows users to make transactions on the blockchain
 - allows users to **create wallets**
 - provides wallets with public and private keys using Elliptic-Curve Cryptography.
 - secures the transfer of funds, by using a digital signature algorithm to prove ownership
 - is a simple model of how a cryptocurreny works

## Hashing
Each block doesn’t just contain the hash of the block before it, but its own hash is in part, calculated from the previous hash. If the previous block’s data is changed then the previous block’s hash will change (since it is calculated in part, by the data) in turn affecting all the hashes of the blocks there after. Calculating and comparing the hashes allow us to see if a blockchain is indeed valid.

The hash is calculated by encrypting the sum of the previous hash, timestamp, nonce (random constant), and merkleRoot (data). The encryption is performed by using the [SHA256 Algorithm](https://en.wikipedia.org/wiki/SHA-2), which was accessed using the java.security package. With this, each block  has its own digital signature based on its information and the signature of the previous block. The method *applySha256* can be found in the StringUtil class.

## The "Chain"
Each block object is stored in an ArrayList to sustain the "chain" integrity of the blockchain. The isChainValid() method loops through all blocks in the chain and compares the hashes to check to see if the hash variable is actually equal to the calculated hash, and the previous block’s hash is equal to the previousHash variable. Any change to the blockchain’s blocks will cause this method to return false.

## Mining
The **proof of work** system is implemented for the mining system for this blockchain, where the longest valid chain is accepted by the network. Proof of work makes the miners try different variable values in the block until its hash starts with a certain number of 0’s. This system means it takes considerable time and computational power to create new blocks. Hence the attacker would need more computational power than the rest of the peers combined.

Nonce value is a constant value that is integrated into the hash. In reality each miner will start iterating from a random point. Some miners may even try random numbers for nonce. Also it’s worth noting that at the harder difficulties solutions may require more than integer.MAX_VALUE, miners can then try changing the timestamp.

The mineBlock() method takes in an int called difficulty, this is the number of 0’s they must solve for. Low difficulties, like 1 or 2, can be solved nearly instantly on most computers, something around 4–6 is ideal for testing. *Fun Fact: At the time of writing Litecoin’s difficulty is around 442,592 *

## Wallet
Coin ownership is transfered on the Blockchain as transactions, participants have an address which funds can be sent to and from. Wallets can just store these addresses. Wallets also hold a private key (used to *sign* the transactions) and a public key (our address).

Both keys are encrypted together using [Elliptic-Curve Cryptography](https://en.wikipedia.org/wiki/Elliptic-curve_cryptography) to generate a *KeyPair*. Private keys ensure that only the owner is able to spend the coins in their wallet. Public key is also sent along with the transaction and it can be used to verify that our signature is valid and data has not been tampered with.

## Transactions
Each transaction will carry ...
 - The public key (address) of the sender
 - The public key (address) of the receiver
 - The value/amount of funds to be transferred
 - Inputs, which are references to previous transactions that prove the sender has funds to send.
 - Outputs, which shows the amount relevant addresses received in the transaction. (These outputs are referenced as inputs in new transactions)
 - A cryptographic signature, that proves the owner of the address is the one sending this transaction and that the data hasn’t been changed
   (for example: preventing a third party from changing the amount sent)
   
Signatures...
- composed of private key + sender address + recipient address + value
- allow only the owner to spend their coins
- prevent others from tampering with their submitted transaction before a new block is mined (at the point of entry)
- verified by miners as a new transaction are added to a block

Transaction outputs will show the final amount sent to each party from the transaction. These, when referenced as inputs in new transactions, act as proof that you have coins to send. Unspent transaction outputs are reffered to as UTXO's.

Blocks in the chain may receive many transactions and the blockchain might be very, very long, it could take eons to process a new transaction because we have to find and check its inputs. To get around this we will keep an extra collection of all unspent transactions that can be used as inputs.

processTransaction() performs some checks to ensure that the transaction is valid, then gather inputs and generating outputs. Afterwards, we discard Inputs from our list of UTXO’s, meaning a transaction output can only be used once as an input. Hence the full value of the inputs must be used, so the sender sends ‘change’ back to themselves.

## BlockChain.java
Tests model by...
 - instantiation of walletA, walletB, and a coinbase
 - A Genesis block that releases 100 coins to walletA
 - walletA sucessfully sends 40 coins to walletB 
 - walletA attempts to send more coins than it has and transaction is denied
 - walletB sucessfully sends 20 coins to walletA
 - blockchain integrity test