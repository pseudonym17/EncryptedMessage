class EncryptionKey(phrase : String) {
	var pLen = 0
	var fChar = 0
	var mChar = 0
	var lChar = 0
	var odd = 3

	init {
		pLen = phrase.length
		var mid = (pLen / 2) - 1
		fChar = (phrase[0].toInt() - 32)
		mChar = (phrase[mid].toInt() - 30)
		lChar = (phrase[pLen-1].toInt() - 22)

		if (pLen % 2 == 0)
			odd = 4
	}

	// Note for later: add chars to message
	fun getEncryptionKey() : Array<Int> {
		var key = emptyArray<Int>()

		// Start by transposing each character
		// by the number of chars in passphrase
		for (i in pLen..(pLen + 94)) {
			if (i > 93) {
				key = append(key, (i - 94))
			}
			else {
				key = append(key, i)
			}
		}

		// Then move every 3rd or 4th character
		for (i in odd..94 step odd) {
			var value = key[i]
			key = pop(key, i)
			key = append(key, value)
		}
		
		// Then move characters many times
		for (i in lChar downTo 1 step 1) {
			for (j in 0..94 step i) {
				var value = key[j]
				key = pop(key, j)
				key = append(key, value)
			}
		}

		return key
	}
}

// Appends an item to the end of an array
fun append(arr: Array<Int>, element: Int): Array<Int> {
	val list: MutableList<Int> = arr.toMutableList()
	list.add(element)
	return list.toTypedArray()
}

// Pops an item from a given location in 
// an array
fun pop(oldArray: Array<Int>, index: Int): Array<Int> {
	if (index < 0 || index >= oldArray.size) {
		return oldArray
	}
	val adjustedArray = oldArray.toMutableList()
	adjustedArray.removeAt(index)
	return adjustedArray.toTypedArray()
}

// Encrypts the message given the key
// Then displays the encrypted message
fun encrypt(key: Array<Int>, message: String) : String{
	var encryptedMessage = ""
	for (i in 0..message.length-1) {
		var charPos = (message[i].toInt() - 32)
		var newChar = (key[charPos] + 32).toChar()
		encryptedMessage = encryptedMessage + newChar
	}

	return encryptedMessage
}

// Decrypts and displays the message
fun decrypt(key: Array<Int>, encrMess: String) : String {
	var decryptedMessage = ""
	for (i in encrMess) {
		for (j in 0..94) {
			if ((key[j] + 32).toChar() == i) {
				var newChar = (j + 32).toChar()
				decryptedMessage = decryptedMessage + newChar
				break
			}
		}
	}
	return decryptedMessage
}

// Gets the passphrase from the user
fun getPassphrase() : String {
	println("\nEnter Your Passphrase: ")
    var passphrase = readLine()!!
    return passphrase
}

// Gets the message from the user, this
// could be the message to encrypt and send
// or the message to decrypt and display
fun getMessage() : String {
	println("\nEnter Your Message: ")
	var message = readLine()!!
	return message
}

// Get the option from the user
fun getOption() : Int {
	println("\nOptions:\n1: Send Message\n2: Decrypt Received Message\n3: Quit\n")
	val option = Integer.valueOf(readLine())
	return option
}

// Main runs the program
fun main() { 
	println("Welcome to Encrypted Messenger")

	var option = getOption()

	while (option == 1 || option == 2) {
		var message = getMessage()
		var phrase = getPassphrase()
		var eK = EncryptionKey(phrase)
		var key = eK.getEncryptionKey()

		when (option) {
			1 -> {
				var encryptedMessage = encrypt(key, message)
				println("\nSending Encrypted Message: $encryptedMessage")
			}
			2 -> {
				var decryptedMessage = decrypt(key, message)
				println("\nDecrypted Message: $decryptedMessage")
			}
		}
		option = getOption()
	}
    
    println("Thank You!")
    
}