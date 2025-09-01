package farid.guliyev.mblockly.ui.screens.output_screen

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import farid.guliyev.mblockly.core.exception_handling.AppException
import farid.guliyev.mblockly.domain.ILLEGAL_EXPRESSION
import farid.guliyev.mblockly.ui.screens.output_screen.parser.Lexer
import farid.guliyev.mblockly.ui.screens.output_screen.parser.evaluateAsBool
import farid.guliyev.mblockly.ui.screens.output_screen.parser.evaluateAsFloat
import farid.guliyev.mblockly.ui.screens.output_screen.parser.parseExpression


fun String.extractFloatValue(
    floatVariables: Map<String, MutableFloatState>,
    onError: (Exception) -> Unit
) : State<Float> {
    return derivedStateOf {
        try {
            val expression = runCatching { Lexer(this).parseExpression() }.getOrElse { throw AppException.FatalException(ILLEGAL_EXPRESSION.format(this)) }
            runCatching { expression.evaluateAsFloat(floatVariables) }.getOrElse { throw AppException.FatalException("Make sure variables exist before usage in $this") }
        } catch (e: Exception) {
            onError(AppException.FatalException(e.message.orEmpty()))
            return@derivedStateOf 0F
        }
    }
}

fun String.extractBooleanValue(
    floatVariables: Map<String, MutableFloatState>,
    onError: (Exception) -> Unit
) : State<Boolean> {
    return derivedStateOf {
        try {
            val expression = runCatching { Lexer(this).parseExpression() }.getOrElse { throw AppException.FatalException(ILLEGAL_EXPRESSION.format(this)) }
            runCatching { expression.evaluateAsBool(floatVariables) }.getOrElse { throw AppException.FatalException("Make sure variables exist before usage in $this") }
        } catch (e: Exception) {
            onError(AppException.FatalException(e.message.orEmpty()))
            return@derivedStateOf false
        }
    }
}