# cyberpay-androidx
[![](https://jitpack.io/v/cyberspace-ltd/cyberpay-androidx.svg)](https://jitpack.io/#cyberspace-ltd/cyberpay-androidx)

Integrate payment in less than a minute with `ONE` line of code.
The Android SDK to integrate to the cyberpay payment gateway
The Cyberpay SDK makes it quick and easy to build seamless payment into your android application. The SDK contains custom views, and helps in quick integration of the .

```kotlin
	CyberpaySdk.checkoutTransaction(this, transaction, object : TransactionCallback() { ... })
```

---

```java
 	CyberpaySdk.INSTANCE.checkoutTransaction(MainActivity.this, transaction, new TransactionCallback() { ... });
```

**Features:**
## Features
- The SDK provides custom native UI elements to get you started easily without having to design the elements yourself.
- Single implementation across 


<!-- prettier-ignore -->
|     | client-only | client-and-server
:--- | :---: | :---:
 **Custom built checkout page.** Create a custom payment page with your business logo and name. | ✅  | ✅ |
 **Dynamic checkout amounts.** Dynamically define product amounts rather than relying on predefined SKUs.  | ❌  | ✅ |
 **Capture payments later.** Optionally split the capture and authorization steps to place a hold on the card and charge later. | ❌ | ✅ |

### Client-only flowchart


## Requirements
The Cyberpay Android SDK is compatible with Android Apps supported from Android 4.1 (Jelly Bean).


## Getting Started

### Install and Configure the SDK
1. Add it in your root build.gradle at the end of repositories:

```
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


2. Add the dependency
```

	dependencies {
	       implementation 'com.github.cyberspace-ltd:cyberpay-androidx:0.0.2'
	}
  
```


### Configure your Cyberpay integration in your Application Class
**Step 1**: Configure API Keys
After installation of the Cyberpay SDK, configure it with your API Integration Key gotten from your merchant dashboard, for test and production

> Java
```java
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //Test Environment
		CyberpaySdk.INSTANCE.initialiseSdk("TEST INTEGRATION KEY", Mode.Debug);
		
		//Live Environment
        //CyberpaySdk.INSTANCE.initialiseSdk("LIVE INTEGRATION KEY", Mode.Live)
		
		// Optional set your company logo to overrride default Cyberpay Logo
        CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)

    }
}

```
> Kotlin
```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()
		
	//Test Environment
        CyberpaySdk.initialiseSdk("TEST INTEGRATION KEY", Mode.Debug)
		
	//Live Environment
        //CyberpaySdk.initialiseSdk("LIVE INTEGRATION KEY", Mode.Live)
		
	// Optional set your company logo to overrride default Cyberpay Logo
        CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)
    }
}
```

---

## Making Payments

**Easy Approach:**


This creates a custom checkout page with just a few lines of code
This allows the user implement payment without having to worry about the boiler plate of implenmentation
We have abstracted the Card details input and validation for you.

First you need to set Transaction Object, set amount and customer Email Address


##Set your Transaction Object
> Java 
```java
	 Transaction trans = new Transaction();
	 `Note Amount is in Kobo so you should multiply by 100` 
	 trans.setAmount(100000.0);
	 trans.setCustomerEmail("test@test.com");
	 
```
> Kotlin
```kotlin

 	 var trans = Transaction()
	 `Note Amount is in Kobo so you should multiply by 100` 
	 trans.amount = 1000000.0
	 trans.customerEmail = "name@email.com"
```	

---


<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/screenrecord.gif" width="30%" />


##Initiate Transaction
> Java

```java
	CyberpaySdk.INSTANCE.checkoutTransaction(this, trans, new TransactionCallback() {
	
        @override 
	public void onSuccess(transaction: Transaction) {
            
        }

        @override 
	public void onError(transaction: Transaction, throwable: Throwable) {

        }

        @Override 
	public void onValidate(transaction: Transaction) {

        }
    });	
```

> Kotlin

```kotlin
CyberpaySdk.checkoutTransaction(this, trans, object : TransactionCallback() {
        override fun onSuccess(transaction: Transaction) {
            //transaction is succesfull
        }

        override fun onError(transaction: Transaction, throwable: Throwable) {
            //transaction error occured
        }

        override fun onValidate(transaction: Transaction) {

        }
    })
```

<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/secure3dpayment.gif" width="30%" />


### Cyberpay payment page & One Time Payment passcode modes for mastercard payment
Here first we have to choose between two flavors (see below for details), then we'll have to provide a callback that handles the tab switch trigger (This is required so that our UI element that also contain the state of the selected tab can update itself - aka switching the tabs always triggered by the application never by FragNav).

|           CYBERPAY_HOME_SCREEN                   |          CYBERPAY_OTP_PAGE           |
| :--------------------------------------: | :----------------------------------: |
| 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/cyberpay.png" width="30%" />
 | 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/otp_screen.png" width="30%" />
 |



---

**Long Approach:**
## Inititalize the Variables
> Java 
```java

	Transaction trans = new Transaction();
	Card card = new Card();
	
	
```
> Kotlin
```kotlin
	var trans = Transaction()
	var card = Card()
```

---

##Initialize your Card Object
> Java

```java

	Card card = new Card();
	card.number = "5399 8300 0000 0008";
    	card.expiryMonth = 5; 
    	card.expiryYear = 30; 
    	card.cvv = "000";
```

> Kotlin
```kotlin

    val card = Card()
    card.number = "5399 8300 0000 0008"
    card.expiryMonth = 5 
    card.expiryYear = 30 
    card.cvv = "000"
```

##
> Java

```java
	CyberpaySdk.getPayment(this, trans, new TransactionCallback() {
	
        @override 
	public void onSuccess(transaction: Transaction) {
            //transaction is succesfull
        }

        @override 
	public void onError(transaction: Transaction, throwable: Throwable) {
            //transaction error occured
        }

        @Override 
	public void onValidate(transaction: Transaction) {

        }
    });	
```

> Kotlin

```kotlin

CyberpaySdk.getPayment(this, trans, object : TransactionCallback() {
	    override fun onSuccess(transaction: Transaction) {
	        //transaction is succesfull
	    }
	
	    override fun onError(transaction: Transaction, throwable: Throwable) {
		//transaction error occured
	    }
	
	    override fun onValidate(transaction: Transaction) {
	
	    }
	})

```

---

**Note** : Ensure when going live, you initialize the Live API key `CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Live)` instead of the Test API key `CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Debug)`. 
This key can be gotten from the merchant dashboard on the cyberpay merchant portal

**Demo**

The demo is running in test mode -- use `5399 8300 0000 0008` as a test card number with CVC: 000, future expiration date: 05/30.

OTP: 123456


Use the `4000 0000 0000 0622` test card number to trigger a 3D Secure payment flow with CVC: 535 future expiration date: 01/20..

---
## FAQs

Q: Where can I get my integration key?

A: 
## Author(s)

[@mobile-cyberpay](https://)