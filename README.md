# The Phone Emulator Idea

I recently had to switch phone service providers.

The retirement of 3G technology means the end of Speakout 7-11 pay-as-you-go service.

The alternative on Lucky Mobile is a comparable alternative.

Then I had an idea. I wanted to see if I can write a phone app that only uses WiFi to dial a call.

However that is not possible by design.


>While you can programmatically open the dialer with a specific number using Intent.ACTION_DIAL and
>a tel: URI, this still requires user interaction to complete the call. The CALL_PRIVILEGED
>permission, which would theoretically allow direct calling, is restricted to system applications and
>cannot be granted to third-party apps. Therefore, any implementation in Jetpack Compose must follow
>this limitation and cannot bypass the dialer interface for emergency numbers.


As a consolation prize, I figured out how to configure a text field to allow a valid phone number
to be entered.

Some key learning points:
- the text field cannot set a maximum length like its XML predecessor. The onValueChanged action
  must have its own logic to trim the text to match the maximum length of the phone number.
- the API level 35 emulator appears to have a bug. The phone soft keyboard set in keyboardOptions
  does not appear.
- the visual transformation can be set to a custom phone number transformation.
- the offset mapping used in the filter() funcdtion of visual transformation must be very precise.
  This was especially true since I chose the transformed number to look like:
  +1 (333) 444-5555