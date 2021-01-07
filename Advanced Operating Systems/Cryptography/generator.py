from NosCrypto import *

def print_key(key):
    return '\n    '.join(key[i:i+60] for i in range(0, len(key), 60))

def print_key_size(key_size):
    return '{:04x}'.format(key_size*8)


# -------------------- collect user's encryption preference -------------------------------

print "*** Izrada digitalne omotnice, digitalnog potpisa te digitalnog pecata ***\n"
print "Program stvara datoteke: \n\tasymmetric_secret_key.txt, asymmetric_public_key.txt\n" \
      "\tsignature.txt, envelope.txt\n" \
	  "\tsymmetric_secret_key.txt, symmetric_crypted_file.txt\n"
print "Odaberite simetrican algoritam:"
print "\t1) DES3"
print "\t2) AES"
odabir_s_alg = input("Unesite broj: ")
if (odabir_s_alg == 1):
    symmetric_algoritam = "DES3"
    print "Odaberite velicinu kljuca:"
    print "\t1) 128 bitova"
    print "\t2) 192 bitova"
    odabir_s_klj = input("Unesite broj: ")
    if (odabir_s_klj == 1):
        KEY_SIZE_SYMM = 16
        IV_SIZE = 8
    elif(odabir_s_klj == 2):
        KEY_SIZE_SYMM = 24
        IV_SIZE = 8
    else: raise NameError
elif (odabir_s_alg == 2):
    symmetric_algoritam = "AES"
    print "Odaberite velicinu kljuca:"
    print "\t1) 128 bitova"
    print "\t2) 192 bitova"
    print "\t3) 256 bitova"
    odabir_s_klj = input("Unesite broj: ")
    if (odabir_s_klj == 1):
        KEY_SIZE_SYMM = 16
        IV_SIZE = 16
    elif(odabir_s_klj == 2):
        KEY_SIZE_SYMM = 24
        IV_SIZE = 16
    elif (odabir_s_klj == 3):
        KEY_SIZE_SYMM = 24
        IV_SIZE = 16
    else: raise NameError
else: raise NameError
print "Odaberite nacin kriptiranja:"
print "\t1) CBC"
print "\t2) OFB"
print "\t3) CFB"
odabir_s_mode = input("Unesite broj: ")
if (odabir_s_mode == 1):
    mode = 'CBC'
elif(odabir_s_mode == 2):
    mode = 'OFB'
elif (odabir_s_mode == 3):
    mode = 'CFB'
else: raise NameError
print "Odaberite velicinu RSA kljuca:"
print "\t1) 1024 bitova"
print "\t2) 2048 bitova"
print "\t3) 3072 bitova"
odabir_as_key = input("Unesite broj: ")
if (odabir_as_key == 1):
    KEY_SIZE_RSA = 128
elif(odabir_as_key == 2):
    KEY_SIZE_RSA = 256
elif (odabir_as_key == 3):
    KEY_SIZE_RSA = 384
else: raise NameError
print "Odaberite hash funkciju:"
print "\t1) SHA-256 bitova"
print "\t2) SHA-512 bitova"
odabir_sha = input("Unesite broj: ")
if (odabir_sha == 1):
    SHA_mode = 'SHA-256'
    KEY_SIZE_SHA = 32
elif(odabir_sha == 2):
    SHA_mode = 'SHA-512'
    KEY_SIZE_SHA = 64
else: raise NameError
print ""
p = raw_input("Unesite poruku (eng. plaintext): ")


# ------------------------ symmetric part -------------------------------------------------
symmetric_key = generate_key(KEY_SIZE_SYMM)
iv = generate_iv(IV_SIZE)

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Secret key\n\n"
data += "Method:\n    " + symmetric_algoritam + "\n    " + mode + "\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_SYMM) + "\n\n"
data += "Secret key:\n    " + print_key(symmetric_key) + "\n\n"
data += "Initialization vector:\n    " + print_key(iv) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('symmetric_secret_key.txt', 'w')
file.write(data)
file.close()

if (symmetric_algoritam == 'DES3'):
    c = DES3_encrypt(p, symmetric_key, iv, mode)
elif (symmetric_algoritam == 'AES'):
    c = AES_encrypt(p, symmetric_key, iv, mode)
else: raise NameError

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Crypted file\n\n"
data += "Method:\n    " + symmetric_algoritam + "\n    " + mode + "\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_SYMM) + "\n\n"
data += "Data:\n    " + print_key(c) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('symmetric_crypted_file.txt', 'w')
file.write(data)
file.close()


# ---------------------------- asymmetric part ------------------------------------------------
key_RSA = RSA.generate(KEY_SIZE_RSA*8)
secret_key_RSA = key_RSA.exportKey('DER')
public_key_RSA = key_RSA.publickey().exportKey('DER')

n = hex(key_RSA.n).replace("L","").replace("0x","")
d = hex(key_RSA.d).replace("L","").replace("0x","")
e = hex(key_RSA.e).replace("L","").replace("0x","")

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Secret key\n\n"
data += "Method:\n    RSA\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_RSA) + "\n\n"
data += "Modulus:\n    " + print_key(n) + "\n\n"
data += "Private exponent:\n    " + print_key(d) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('asymmetric_secret_key.txt', 'w')
file.write(data)
file.close()

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Public key\n\n"
data += "Method:\n    RSA\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_RSA) + "\n\n"
data += "Modulus:\n    " + print_key(n) + "\n\n"
data += "Public exponent:\n    " + print_key(e) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('asymmetric_public_key.txt', 'w')
file.write(data)
file.close()


# --------------------------- envelope part ----------------------------------------------------
encrypted_symmetric_key = RSA_encrypt(symmetric_key, public_key_RSA)
encrypted_iv = RSA_encrypt(iv, public_key_RSA)

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Envelope\n\n"
data += "Method:\n    RSA\n    " + symmetric_algoritam + "\n    " + mode + "\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_RSA) + "\n    " + print_key_size(KEY_SIZE_SYMM) + "\n\n"
data += "Envelope data:\n    " + print_key(c) + "\n\n"
data += "Envelope crypt key:\n    " + print_key(encrypted_symmetric_key) + "\n\n"
data += "Envelope crypt vector:\n    " + print_key(encrypted_iv) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('envelope.txt', 'w')
file.write(data)
file.close()


# --------------------------- signature part ----------------------------------------------------
if (SHA_mode == 'SHA-256'):
    hash_SHA = SHA256_hash(p)
elif (SHA_mode == 'SHA-512'):
    hash_SHA = SHA512_hash(p)
else: raise NameError

encrypted_SHA = RSA_encrypt(hash_SHA, public_key_RSA)

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Signature\n\n"
data += "Method:\n    " + SHA_mode + "\n    RSA\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_SHA) + "\n    " + print_key_size(KEY_SIZE_RSA) + "\n\n"
data += "Signature:\n    " + print_key(encrypted_SHA) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('signature.txt', 'w')
file.write(data)
file.close()


# --------------------------- stamp part ----------------------------------------------------

data = "---BEGIN OS2 CRYPTO DATA---\n"
data += "Description:\n    Stamp\n\n"
data += "Method:\n    RSA\n    " + SHA_mode + "\n    " + symmetric_algoritam + "\n    " + mode + "\n\n"
data += "Key length:\n    " + print_key_size(KEY_SIZE_RSA) + "\n    " + print_key_size(KEY_SIZE_SHA) + "\n    " + print_key_size(KEY_SIZE_SYMM) + "\n\n"
data += "Envelope data:\n    " + print_key(c) + "\n\n"
data += "Signature:\n    " + print_key(encrypted_SHA) + "\n\n"
data += "Envelope crypt key:\n    " + print_key(encrypted_symmetric_key) + "\n\n"
data += "Envelope crypt vector:\n    " + print_key(encrypted_iv) + "\n\n"
data += "---END OS2 CRYPTO DATA---\n"
print data
file = open('stamp.txt', 'w')
file.write(data)
file.close()

print "-> Sve datoteke su stvorene."
raw_input("Press Enter to exit...")
