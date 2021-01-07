# Lab 1
# Crpytographic operations (secret key, public key and hash algorithms)

You need to fetch the latest version of assignment from the repository:

```
$ cd sui-lab/zadatak1
$ git pull
```

This assignment consists of 5 parts.

## 1) Signature verification and decryption
You have 4 files at your disposal and they are located in the folder
`zadatak1`. With the file command you can check the type of these files:
```console
# file *
data.bin:    GPG symmetrically encrypted data (AES256 cipher)
key.gpg:     PGP public key block Public-Key (old)
PROCITAJ_ME: UTF-8 Unicode text
README:	     ASCII text
secret.bin:  data
```
File description:

- `key.gpg` - public key for signature verification
- `secret.bin` - digitally signed and compressed symmetric passphrase
- `data.bin` - symmetrically encrypted data
- (`PROCITAJ_ME` - readme in Croatian)

The file `key.gpg` contains the public key that you need to import into the GnuPG
key database. This key is used to verify the digital signature that is contained
within the file `secret.bin`. This file contains information about the symmetric
passphrase and the digital signature and it's formatted as a GnuPG compressed
binary.

After the signature file is checked the compressed contents are
displayed in the terminal. You need to derive the passphrase from that data.
With this passphrase you need to decrypt the contents of the file `data.bin`.

After you decrypt the `data.bin` file, you need to check whether it has the
following SHA-256 hash:
```console
3059724a4a32088244560552b09bb425db76f71c6143ed0af20ae62b7861ea96
```

## 2) Viewing the decrypted data
After you've obtained the needed file, with the file command you need to
check the file type and add the needed extension to the filename. Then you can
transfer that file on your PC and open it with the appropriate viewer.

## 3) Digital signatures
You need to generate your own private key. Input your own data when asked
(name, surname, email). Create a text file that contains your name, surname and
student identification number, i.e.:
```text
John Doe 0036123456
```

After you've created the file you need to digitally sign it. Export your public
key from the GnuPG database. The public key must be in the following format:
```console
-----BEGIN PGP PUBLIC KEY BLOCK-----
Version: GnuPG v2.0.22 (GNU/Linux)
mQMuBF....r+91D
-----END PGP PUBLIC KEY BLOCK-----
```

## 4) Secret data encryption
Create a new file that contains some secret data (e.g.):
```console
$ cat mysecret
This is a secret that only sui@fer.hr needs to know.
```
Encrypt this file by using gpg and the public key (`key.gpg`) that you got in the
assingment (key for sui@fer.hr).

## 5) Secret key and public key encryption speed
The last part of the assignments has to do with measuring the speed of secret
key and public key encryption by using the openssl tool

### a) Secret key cryptography (AES and 3DES)
Generate random data for symmetric encryption tests (50MB):
```console
$ dd if=/dev/urandom of=/tmp/data bs=10K count=5000
```
Measure the encryption and decryption speeds for the following algorithms and
modes of operation:

- aes-256-ecb
- des-ede3
- aes-256-cbc
- des-ede3-cbc
- aes-256-ctr

Encryption/decryption examples:
```console
$ time openssl enc -aes-256-ecb -in /tmp/data -out /tmp/data.aes.ecb -k sui_key
$ time openssl enc -d -aes-256-ecb -in /tmp/data.aes.ecb -out /tmp/data.ecb -k sui_key
# Check for differences between the original data and decrypted data:
$ diff /tmp/data /tmp/data.ecb
```
Compare the algorithm speed and comment on the results. Which algorithm is
faster? Which algorithm is safer to use?

### b) Public key cryptography (RSA and ECDSA)
Start the automated openssl RSA and ECDSA speed tests (one at a time to avoid
innacurate measurements):
```console
$ openssl speed rsa
$ openssl speed ecdsa
```
What is faster, digital signing or verifying the digital signature?

Calculate the ratio for thess two operations for all both algorithms and all key
sizes.

## Assignment results

You **need to send** the following data by email to
[sui@fer.hr](mailto:sui@fer.hr) after solving the assignment: 

- **report** on the subject in **PDF format** (should not exceed **500 words**)
  which contains the procedure that you used to solve the assigment along with
  answers to assignment questions
- **public key** and **digitally signed data** from part 3
- **encrypted data** from part 4

## Tools for this assignment

- gpg - encryption, decryption and key generation
- file - determine file types
- sha256sum - calculate SHA-256 hash value
- dd - copying binary data
- openssl - encryption, decryption and key generation
- time - measuring execution time
- WinSCP/scp - tool to transfer the data from the virtual image to PC
