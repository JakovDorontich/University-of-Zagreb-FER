from Crypto.Cipher import DES3, AES
from Crypto.PublicKey import RSA
from hashlib import sha256, sha512
import os
import base64

# Za zadanu velicinu (u bajtovima/oktetima) generira kljuc
# koji je predtavljen kao heksadecimalan zapis
def generate_key(key_size):
    key = os.urandom(key_size/2).encode('hex')
    return key

# Za zadanu velicinu (u bajtovima/oktetima) generira iv vektor
# koji je predtavljen kao heksadecimalan zapis
def generate_iv(iv_size):
    iv = os.urandom(iv_size/2).encode('hex')
    return iv

# Ulazni podatak se nadopunjuje do prikladne velicine bloka
def pad(input, blockSize):
    missing = blockSize-len(input)%blockSize
    return input + (missing * chr(missing))

# Ulaznom tekstu se makne visak dodan pad() metodom
def unpad(input):
    return input[:-ord(input[len(input)-1:])]

# ----------------------------------------------------------------------------------------

# key i iv moraju biti hex oblika
# rezultat vraca base64 oblik
def DES3_encrypt(plaintext, key, iv, mode):
    if (mode.upper() == 'CBC'):
        cipher = DES3.new(key, DES3.MODE_CBC, iv)
    elif (mode.upper() == 'OFB'):
        cipher = DES3.new(key, DES3.MODE_OFB, iv)
    elif (mode.upper() == 'CFB'):
        cipher = DES3.new(key, DES3.MODE_CFB, iv)
    else:
        raise NameError
    plaintext = pad(plaintext, len(iv))
    ciphertext = cipher.encrypt(plaintext)
    return base64.b64encode(ciphertext)

# key i iv moraju biti hex oblika
# rezultat vraca izvornu poruku
def DES3_decrypt(ciphertext, key, iv, mode):
    if (mode.upper() == 'CBC'):
        cipher = DES3.new(key, DES3.MODE_CBC, iv)
    elif (mode.upper() == 'OFB'):
        cipher = DES3.new(key, DES3.MODE_OFB, iv)
    elif (mode.upper() == 'CFB'):
        cipher = DES3.new(key, DES3.MODE_CFB, iv)
    else:
        raise NameError
    ciphertext = base64.b64decode(ciphertext)
    plaintext = unpad(cipher.decrypt(ciphertext))
    return plaintext

# key i iv moraju biti hex oblika
# rezultat vraca base64 oblik
def AES_encrypt(plaintext, key, iv, mode):
    if (mode.upper() == 'CBC'):
        cipher = AES.new(key, AES.MODE_CBC, iv)
    elif (mode.upper() == 'OFB'):
        cipher = AES.new(key, AES.MODE_OFB, iv)
    elif (mode.upper() == 'CFB'):
        cipher = AES.new(key, AES.MODE_CFB, iv)
    else:
        raise NameError
    plaintext = pad(plaintext, len(iv))
    ciphertext = cipher.encrypt(plaintext)
    return base64.b64encode(ciphertext)

# key i iv moraju biti hex oblika
# rezultat vraca izvornu poruku
def AES_decrypt(ciphertext, key, iv, mode):
    if (mode.upper() == 'CBC'):
        cipher = AES.new(key, AES.MODE_CBC, iv)
    elif (mode.upper() == 'OFB'):
        cipher = AES.new(key, AES.MODE_OFB, iv)
    elif (mode.upper() == 'CFB'):
        cipher = AES.new(key, AES.MODE_CFB, iv)
    else:
        raise NameError
    ciphertext = base64.b64decode(ciphertext)
    plaintext = unpad(cipher.decrypt(ciphertext))
    return plaintext

# key mora biti bin oblika
# rezultat vraca hex oblik
def RSA_encrypt(plaintext, publicKey):
    pk = RSA.importKey(publicKey)
    ciphertext = pk.encrypt(plaintext, 'X')[0]
    return ciphertext.encode('hex')

# key mora biti bin oblika
# rezultat vraca izvornu poruku
def RSA_decrypt(ciphertext, secretKey):
    sk = RSA.importKey(secretKey)
    plaintext = sk.decrypt(ciphertext.decode('hex'))
    return plaintext

def SHA256_hash(input):
    return sha256(input).hexdigest()

def SHA512_hash(input):
    return sha512(input).hexdigest()

