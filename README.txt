UTEID: sbp694;
FIRSTNAME: Samuel;
LASTNAME: Pritchett;
CSACCOUNT: samuel34;
EMAIL: johnyd@cs.utexas.edu; bradp@cs.utexas.edu;

[Program 5]
[Description]
This program was written in a single file called "AES.java". The program takes in three parameters. Either "e" or "d" to indicate whether it was encrypting or decrypting the given input file, and input file and a file containing the key. The methods "readInKey" and "formatKey" were used to read in the key and arrange it in a 4x8 2D array to ease the key expansion process. Once the formatting was done, the key was then passed to the "keyExpansion" method where the AES-256 key expansion algorithm was applied to the key to make it a 4x60 array. After the key expansion was finish, it was passed on to the encryption/decryption portion where a line of the plaintext was read in and arraged into a 4x4 array called the state. The state then went through 14 rounds of the AES algorithm using the methods "addRoundKey", "getRoundKey", "subBytes", "shiftRows" and "mixColumns". After the state had gone through all 14 rounds of the AES algorithm, the output was then written to a file with its respective file name depending on whether encryption or decryption was applied. 

[Finish]
I completed the entire assignment. However, I ran into a small problem an hour before having to turn it in. Turns out that java Scanner objects cannot handle files ending in ".enc" and which resulted in me getting "NullPointerExceptions". Therefore as a quick fix before running out of time, I made the output file for encryption to be the following, inputFileName + "enc.txt" so the scanner can read the file correctly when decrypting.

[Test Case 1]

[Command line]
java AES e zeroKey testvector1

[Timing Information]
Duration: 105496739 nanoseconds
Total bytes: 1600
Throughput in megabytes / second = .01517

[Input Filenames]
testvector1

[Output Filenames]
testvector1enc.txt



[Test Case 2]

[Command line]
java AES d zeroKey testvector1enc

[Timing Information]
Duration: 122940647 nanoseconds
Total bytes: 1600
Throughput in megabytes / second = .013014

[Input Filenames]
testvector1enc

[Output Filenames]
testvector1enc.dec



[Test Case 3]

[Command line]
java AES e incKey testvector2

[Timing Information]
Duration: 19037073 nanoseconds
Total bytes: 1600
Throughput in megabytes / second = .08405

[Input Filenames]
testvector2

[Output Filenames]
testvector2enc


[Test Case 4]

[Command line]
java AES d incKey testvector2enc

[Timing Information]
Duration: 20825242 nanoseconds
Total bytes: 1600
Throughput in megabytes / second = .07683

[Input Filenames]
testvector2enc

[Output Filenames]
testvector2enc.dec

