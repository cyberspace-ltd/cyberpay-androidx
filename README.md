# cyberpay-androidx
[![](https://jitpack.io/v/cyberspace-ltd/cyberpay-androidx.svg)](https://jitpack.io/#cyberspace-ltd/cyberpay-androidx)


The Android SDK to integrate to the cyberpay payment gateway
The Cyberpay SDK makes it quick and easy to build seamless payment into your android application. The SDK contains custom views, and helps in quick integration of the .

**Features:**
## Features
- Localization in 14 different languages 🌍
- Built-in MASTER CARD support 🌍
- Built-in VISA CARD support 🌍
- Built-in VERVE CARD support 🌍
- Built-in dynamic 3D Secure payment  🔔
- Support for customers in the Nigeria 🇳G🌷 
- Plans to support more payment methods 🔮

- The SDK provides custom native UI elements to get you started easily without having to design the elements yourself.


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
```java
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //Test Environment
        CyberpaySdk.initialiseSdk(" TEST INTEGRATION KEY", Mode.Debug)
		
		//Live Environment
        //CyberpaySdk.initialiseSdk("LIVE INTEGRATION KEY", Mode.Live)
		
		
		// Optional set your company logo to overrride default Cyberpay Logo
        CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)

    }
}

```

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

**Note** : Ensure when going live, you initialize the Live API key `CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Live)` instead of the Test API key `CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Debug)`. 
This key can be gotten from the merchant dashboard on the cyberpay merchant portal

**Demo**

See the sample [live](https://0hczv.sse.codesandbox.io/) or [fork](https://codesandbox.io/s/stripe-sample-checkout-one-time-payments-0hczv) on CodeSandbox.

The demo is running in test mode -- use `5399 8300 0000 0008` as a test card number with CVC: 000, future expiration date: 05/30.

OTP: 123456


Use the `4000 0000 0000 0622` test card number to trigger a 3D Secure challenge flow with CVC: 535 future expiration date: 01/20..

Read more about testing on Stripe at https://cyberpay.com/docs/testing.

<details open><summary>USD Cards Demo</summary>
<img src="./checkout-demo.gif" alt="A gif of the Checkout payment page rendering" align="center">
</details>

<details><summary>EUR Cards & iDEAL Demo</summary>
<img src="./checkout-demo-ideal.gif" alt="A gif of the Checkout payment page rendering" align="center">
</details>

**Easy Approach:**

##Initialise your Tran
> Java 
```java
	 CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Debug);
	 
	 // Optional set your company logo to overrride default Cyberpay Logo
     CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card);
```
> Kotlin
```kotlin

 	CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Debug)
	
	 // Optional set your company logo to overrride default Cyberpay Logo
    CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)
```	

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

---
## Make Transaction
> Java

```java
	CyberpaySdk.checkoutTransaction(this, trans, new TransactionCallback() {
	
        @override 
		public void onSuccess(transaction: Transaction) {
            Log.e("RESPONSE", "SUCCESSFUL")
            Log.e("TRANSACTION", transaction.reference)
            Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)
        }

        @override 
		public void onError(transaction: Transaction, throwable: Throwable) {
            Log.e("ERROR", throwable.message!!)
            Log.e("TRANSACTION", transaction.reference)
            Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)

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
            Log.e("RESPONSE", "SUCCESSFUL")
            Log.e("TRANSACTION", transaction.reference)
            Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)
        }

        override fun onError(transaction: Transaction, throwable: Throwable) {
            Log.e("ERROR", throwable.message!!)
            Log.e("TRANSACTION", transaction.reference)
            Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)

        }

        override fun onValidate(transaction: Transaction) {

        }
    })
```

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

##
> Java

```java
	CyberpaySdk.getPayment(this, trans, new TransactionCallback() {
	
        @override 
		public void onSuccess(transaction: Transaction) {
            Log.e("RESPONSE", "SUCCESSFUL")
            Log.e("TRANSACTION", transaction.reference)
            Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)
        }

        @override 
		public void onError(transaction: Transaction, throwable: Throwable) {
            Log.e("ERROR", throwable.message!!)
            Log.e("TRANSACTION", transaction.reference)
            Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)

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
	        Log.e("RESPONSE", "SUCCESSFUL")
	        Log.e("TRANSACTION", transaction.reference)
	        Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)
	    }
	
	    override fun onError(transaction: Transaction, throwable: Throwable) {
	        Log.e("ERROR", throwable.message!!)
	        Log.e("TRANSACTION", transaction.reference)
	        Log.e("TRANSACTION-MERCHANT", transaction.merchantReference)
	
	    }
	
	    override fun onValidate(transaction: Transaction) {
	
	    }
	})

```

**Features:**

- Localization in 14 different languages 🌍
- Built-in MASTER CARD support 🌍
- Built-in VISA CARD support 🌍
- Built-in VERVE CARD support 🌍
- Built-in dynamic 3D Secure payment (ready for SCA) 🔔
- Support for iDEAL for customers in the Nigeria 🇳G🌷 (NIG only; Server only)
- Plans to support more payment methods 🔮

For more features see the [Checkout documentation](https://stripe.com/docs/payments/checkout).

There are two integrations: [client-only](./client-only) and [client-and-server](./client-and-server).

<!-- prettier-ignore -->
|     | client-only | client-and-server
:--- | :---: | :---:
🔨 **Prebuilt checkout page.** Create a payment page that is customizable with your business' name and logo easy to use, easy to integrate. | ✅  | ✅ |
🔢 **Dynamic checkout amounts.** Dynamically define product amounts rather than relying on predefined SKUs.  | ❌  | ✅ |
⌛ **Capture payments later.** Optionally split the capture and authorization steps to place a hold on the card and charge later. | ❌ | ✅ |

### Client-only flowchart

<img src="https://storage.googleapis.com/stripe-samples-flow-charts/checkout-one-time-client-only.png" alt="A flowchart of the Checkout flow" align="center">

### Client-and-server flowchart

<img src="https://storage.googleapis.com/stripe-samples-flow-charts/checkout-one-time-client-server.png" alt="A flowchart of the Checkout flow" align="center">

## How to run locally

There are two integrations: `client-only` and `client-and-server`. The following are instructions on how to run the `client-and-server` integration:

This sample includes 5 server implementations in Node, Ruby, Python, Java, and PHP.

Follow the steps below to run locally.

**[Client-only] 0. Enable Checkout in the Dashboard**

If you plan on using the client-only integration, go to your [account settings](https://dashboard.stripe.com/account/checkout/settings) in the Dashboard and enable Checkout.

**1. Clone and configure the sample**

The Stripe CLI is the fastest way to clone and configure a sample to run locally.

**Using the Stripe CLI**

If you haven't already installed the CLI, follow the [installation steps](https://github.com/stripe/stripe-cli#installation) in the project README. The CLI is useful for cloning samples and locally testing webhooks and Stripe integrations.

In your terminal shell, run the Stripe CLI command to clone the sample:

```
stripe samples create checkout-one-time-payments
```

The CLI will walk you through picking your integration type, server and client languages, and configuring your .env config file with your Stripe API keys.

**Installing and cloning manually**

If you do not want to use the Stripe CLI, you can manually clone and configure the sample yourself:

```
git clone https://github.com/stripe-samples/checkout-one-time-payments
```

Copy the .example file into a file named .env in the folder of the server you want to use. For example:

```
cp .env.example client-and-server/server/node/.env
```

You will need a Cyberpay account in order to run the demo. Once you set up your account, go to the Cyberpay [developer dashboard](https://stripe.com/docs/development#api-keys) to find your API keys.

```
CYBERPAY_PUBLISHABLE_KEY=<replace-with-your-publishable-key>
CYBERPAY_SECRET_KEY=<replace-with-your-secret-key>
```

The other environment variables are configurable:

`STATIC_DIR` tells the server where to the client files are located and does not need to be modified unless you move the server files.

`BASE_PRICE` is the amount for the order.

`CURRENCY` is the currency for the order.

`DOMAIN` is the domain of your website, where Checkout will redirect back to after the customer completes the payment on the Checkout page.

**2. Follow the server instructions on how to run:**

Pick the server language you want and follow the instructions in the server folder README on how to run.

For example, if you want to run the Node server:

```
cd client-and-server/server/node # there's a README in this folder with instructions
npm install
npm start
```

## FAQ

Q: Why did you pick these frameworks?

A: We chose the most minimal framework to convey the key Cyberpay calls and concepts you need to understand. These demos are meant as an educational tool that helps you roadmap how to integrate Cyberpay within your own system independent of the framework.

Q: Can you show me how to build X?

A: We are always looking for new sample ideas, please email david.ehighiator@cyberspace.net.ng with your suggestion!

## Author(s)

[@david-cyberpay](https://twitter.com/david)
[@sunday-cyberpay](https://twitter.com/sunday)
[@shaba-cyberpay](https://twitter.com/shaba)