from NosCrypto import *

# pravilno ucitavanja zapisa (najcesce kljuca) koji se mozda
# proseze kroz vise linija u datoteci
def load_key(array, word):
    result = ''
    for i in range(len(array)):
        if (array[i].strip() == word):
            tmp = i+1
            while(array[tmp] != '\n'):
                result += array[tmp].strip()
                tmp += 1
            break
    return result


print "*** Dekripcija i validacija iz zadanih podataka ***\n"
print "Program koristi samo ove datoteke: \n\tasymmetric_secret_key.txt, signature.txt, envelope.txt"
print "Koji primjer zelite pokrenuti?"
print "\t1) Primjer1: DES3(128), CBC, RSA(1024), SHA-256"
print "\t2) Primjer2: DES3(192), OFB, RSA(1024), SHA-512"
print "\t3) Primjer3: AES(128),  CBC, RSA(2048), SHA-512"
print "\t4) Primjer4: AES(192),  CFB, RSA(2048), SHA-256"
print "\t5) Primjer5: AES(256),  CBC, RSA(3072), SHA-512"
print "\t6) PrimjerX: Vas primjer"
choice = input("Unesite broj primjera: ")
if (choice == 1): root = "Primjer1"
elif (choice == 2): root = "Primjer2"
elif (choice == 3): root = "Primjer3"
elif (choice == 4): root = "Primjer4"
elif (choice == 5): root = "Primjer5"
elif (choice == 6): root = "PrimjerX"
else: raise NameError

# ------------------------------- load data --------------------------------------------
path = root + '\\asymmetric_secret_key.txt'
file = open(path, 'r')
lines = file.readlines()
asymmetric_secret_key_length = int(lines[8].strip(), 16)/8
modulus = int(load_key(lines[10:], 'Modulus:'), 16)
d = int(load_key(lines[10:], 'Private exponent:'), 16)
file.close()

path = root + '\\signature.txt'
file = open(path, 'r')
lines = file.readlines()
hash_algoritam = lines[5].strip()
hash_key_length = int(lines[9].strip(), 16)/8
encrypted_signature = load_key(lines[12:], 'Signature:')
file.close()

path = root + '\\envelope.txt'
file = open(path, 'r')
lines = file.readlines()
symmetric_algoritam = lines[6].strip()
symmetric_method = lines[7].strip()
c = load_key(lines[12:], 'Envelope data:')
encrypted_symmetric_key = load_key(lines[12:], 'Envelope crypt key:')
encrypted_iv = load_key(lines[12:], 'Envelope crypt vector:')
file.close()


# -------------------------------- description -------------------------------------------
key_RSA = RSA.construct((modulus, 65537L, d))
secret_key_RSA = key_RSA.exportKey('DER')

decrypted_symmetric_key = RSA_decrypt(encrypted_symmetric_key, secret_key_RSA)
decrypted_iv = RSA_decrypt(encrypted_iv, secret_key_RSA)
if (symmetric_algoritam == 'DES3'):
    p = DES3_decrypt(c, decrypted_symmetric_key, decrypted_iv, symmetric_method)
elif(symmetric_algoritam == 'AES'):
    p = AES_decrypt(c, decrypted_symmetric_key, decrypted_iv, symmetric_method)
else: raise NameError

print "\nDekriptirana poruka: \n\n" + p

# -------------------------------- validation -------------------------------------------
decrypted_signature = RSA_decrypt(encrypted_signature, secret_key_RSA)
if (hash_key_length*8 == 256):
    signature_p = SHA256_hash(p)
elif (hash_key_length*8 == 512):
    signature_p = SHA512_hash(p)
else: raise NameError

if (signature_p == decrypted_signature):
    print "\nPotpis je validan!\n"
else:
    print "\nPotpis nije validan!\n"

raw_input("Press Enter to exit...")


