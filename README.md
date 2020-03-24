# Introduction 
Cyberpay provides you with the most convenient and fastest process of making and collecting payments from your customers all over the world

# About the SDK
The mobile SDK will serve as an easy to use library to quickly integrate Cyberpay to your mobile application.

The will serve as a wrapper on the existing Cyberpay web services and create a mobile entry point for making both Card and Bank transactions.

The SDK will provide custom views/layouts for checkout, pin, otp, sucured3d as well as handles all business logics taking the bulk of the job and exposing just three call backs representing the status of the transaction.

The SDK introduces a nice error wrapper class on the primary network component, introducing a painless and detailed error messages.

The SDK is designed and written in Kotlin, using the singleton pattern so only one instance is available throughout the life of the application. 


# Tools Used
## IDE 
Android studio is the official integrated development environment (IDE) for Android development, built on JetBrains IntelliJ IDEA software and designed specifically for android development

## Libraries
### Network
Network calls is the heart of most functional mobile applications, this is no different. Here we used Retrofit

Retrofit is a REST client for Java and Android. It makes it easy to send and retrieve JSON or any structured data via a web-based services.

### ReactiveX
Reactive programming is a software development model structured around asynchronous data streams or sequence. 

ReactiveX (Reactive eXtensions) is a combination of the best ideas from the Observer pattern, the iterator pattern and functional programming.

Here RxJava and RxKotlin are used in combination with Retrofit to make asynchronous network calls


### Unit Testing
Unit testing is the fundamentally carried out to determine the functionality of the smallest possible unit of code

JUnit is the recommended unit testing framework for Java/Android 


# Getting Started
[![](https://jitpack.io/v/cyberspace-ltd/cyberpay-androidx.svg)](https://jitpack.io/#cyberspace-ltd/cyberpay-androidx)

Integrate payment in less than a minute with `ONE` line of code as seen below.

> Kotlin
```kotlin
	CyberpaySdk.checkoutTransaction(this, transaction, object : TransactionCallback() { ... })
```

---

> Java
```java
 	CyberpaySdk.INSTANCE.checkoutTransaction(MainActivity.this, transaction, new TransactionCallback() { ... });
```
---
The Android SDK allows you to integrate with the cyberpay payment gateway seamlessly with just a few STEPS.
The SDK contains Custom Views, and helps in quick integrating a checkout page faster than the blink of your eye.


## Features
- The SDK provides custom native UI elements to get you started easily without having to design the elements yourself.
- Single implementation across 


<!-- prettier-ignore -->
|     | client-only | client-and-server
:--- | :---: | :---:
 **Custom built checkout page.** Create a custom payment page with your business logo and name. | ✅  | ✅ |
 **Dynamic checkout amounts.** Dynamically define product amounts rather than relying on predefined SKUs.  | ❌  | ✅ |
 **Capture payments later.** Optionally split the capture and authorization steps to place a hold on the card and charge later. | ❌ | ✅ |

## Client-only flowchart


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
	   implementation 'com.github.cyberspace-ltd:cyberpay-androidx:0.2.3'
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

### There are Three (3) Approaches to Integrate Cyberpay SDK


## 1) Easy Approach: Simple Checkout Page(No need to implement your custom payment UI)

This creates a prebuilt checkout page with just a few lines of code
This allows the user implement payment without having to worry about the boiler plate of implenmentation
We have abstracted the Card details input and validation for you.

First you need to set Transaction Object, set amount and customer Email Address


### Set your Transaction Object
> Java 
```java
	 Transaction trans = new Transaction();
	 `Note Amount is in Kobo so you should multiply by 100` 
	 trans.setAmount(100000.0);
	 trans.setMerchantReference("YOUR REFERENCE"); // optional. auto generated if not provided
	 trans.setCustomerEmail("test@test.com");
	 
```
> Kotlin
```kotlin

 	 var trans = Transaction()
	 `Note Amount is in Kobo so you should multiply by 100` 
	 trans.amount = 1000000.0
	 trans.merchantReference = "YOUR REFERENCE"// optional. auto generated if not provided
	 trans.customerEmail = "name@email.com"
```	

---



## Initiate Transaction


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

---


|                     CYBERPAY MASTER CARD                           |                       CYBERPAY  SECURE 3D                             |                      CYBERPAY BANK PAYMENT                | 
| :----------------------------------------------------------------: | :-------------------------------------------------------------------: |  :------------------------------------------------------: |
| 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/screenrecord.gif" width="30%" />
 | 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/secure3dpayment.gif" width="30%" />
 |
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/bankpayment.gif" width="30%" />
 |




### Cyberpay payment page & One Time Payment passcode modes for mastercard payment

|           CYBERPAY CHECKOUT PAGE                 |                  CYBERPAY OTP PAGE               |                CYBERPAY CARD PIN PAGE            |
| :----------------------------------------------: | :----------------------------------------------: | :----------------------------------------------: |
| 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/cyberpay.png" width="30%" />
 | 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/otp_screen.png" width="30%" />
 | 
<img src="https://raw.githubusercontent.com/cyberspace-ltd/cyberpay-androidx/dev/cyberpaysdk/src/main/java/com/cyberspace/cyberpaysdk/utils/screenshot/cardpin.png" width="30%" />
 |



---

## 2) Long Approach: Your Custom implementation
If you want to have full control of the implementatuin while mentaining the same look and feel across your screens, 
we got you covered and you can implement the same way with just a few lines of code. 
However, you will have to implement the progressbar and your textfield to get the card details from your users.

## Initialize your Card Object
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

## Initialize your Transaction Object

> Java 
```java

	Transaction trans = new Transaction();
        trans.setMerchantReference("YOUR REFERENCE"); // optional. auto generated if not provided
	trans.setCustomerEmail("mycustomer@email.com");
	trans.setAmout(10000); // amount in kobo
	trans.setCard = card
	
```
> Kotlin
```kotlin
	var trans = Transaction()
	trans.merchantReference = "YOUR REFERENCE" // optional. auto generated if not provided
        trans.customerEmail = "mycustomer@email.com"
        trans.amout = 10000 // amount in kobo
	trans.card = card

```

---



## Implement your custom payment checkout page
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

**Note** : Ensure when going live, you initialize the Live API key `CyberpaySdk.initialiseSdk("LIVE_INTEGRATION_KEY", Mode.Live)` instead of the Test API key `CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Debug)`. 
This key can be gotten from the merchant dashboard on the cyberpay merchant portal

**Demo**

Use the `d5355204f9cf495f853c8f8d26ada19b` integration key on `Debug Mode`..

The demo is running in test mode -- use `5399 8300 0000 0008` as a test card number with CVC: 000, future expiration date: 05/30.

OTP: 123456


Use the `4000 0000 0000 0622` test card number to trigger a 3D Secure payment flow with CVC: 535 future expiration date: 01/20..

---
## FAQs

Q: Where can I get my integration key?

A: You can get your integration key on the merchant dashboad of the Cyberpay official web portal.
## Author(s)

[Mobile Unit](mobileunit@cyberspace.net.ng)
