package com.phone.emulator.ui.transformation

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private const val TAG: String = "The phone number transformation"

class PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return this.phoneNumberFilter(text)
    }
}

fun PhoneNumberTransformation.trimmed(text: String) : String {
    val trimmed = if (text.length >= 10) text.substring(0..9) else text
    return trimmed
}

/**
 * Plain function to create phone number transformation for the visual transformation of the
 * phone number.
 */
fun PhoneNumberTransformation.phoneNumberFilter(text: AnnotatedString): TransformedText {

    // The making of the +1 (XXX) XXX-XXXX

    val trimmed = this.trimmed(text.text)

//    var out = "" // The transformed value to build
    var out = if (trimmed.isNotEmpty()) "+1 (" else ""
    for (i in trimmed.indices) {
        if (i == 6) out += "-"
        out += trimmed[i]
        if (i == 2) out += ") "
    }

    // The offset translator should ignore the +1, parentheses, whitespace, and hyphen,
    // so conversion from original offset to the transformed text works like:
    // aaabbbcccc to +1 (aaa) bbb-cccc
    // 0123456789    01234567890123456  - indices
    // 1234567890    12345678901234567  - offsets
    // - the 0th char of the original text is the 4th char of the transformed text
    // - the 3th char of the original text is the 9th char of the transformed text
    // - the 6th char of the original text is the 13th char of the transformed text
    val phoneNumberOffsetTranslator =
        object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {

                Log.i(TAG, "original-to-transformed function")

                Log.i(TAG, "the original offset is $offset")

                // fine-grained control of every position required with leading characters.
                val transformed = when (offset) {
                    0 -> offset
                    in 1..3 -> offset + 4
                    in 4..6 -> offset + 6
                    in 7..10 -> offset + 7
                    else -> 17
                }

                Log.i(TAG, "the transformed offset is $transformed")

                return transformed

            }

            override fun transformedToOriginal(offset: Int): Int {

                Log.i(TAG, "transformed-to-original function")

                Log.i(TAG, "the transformed offset is $offset")

                // fine-grained control of every position required with leading characters.
                val original = when (offset) {
                    in 0..4 -> 0 //  transformed positions do not exist in original = 0
                    in 5..7 -> offset - 4
                    in 8..11 -> offset - 6
                    in 12..17 -> offset - 7
                    else -> 10
                }

                Log.i(TAG, "the original offset is $original")

                return original

            }

        }

    return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)

}