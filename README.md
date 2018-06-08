# Blockchain
A Java based model of a Blockchain that stores data for cryptocurrency transactions. 

## Model
 - is composed of blocks that **store** transaction data
 - implements a simple mining system
 - has a **digital signature** that chains blocks together
 - requires proof of work mining to validate new blocks
 - can be used to check to see if data in it is **valid and unchanged**
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

