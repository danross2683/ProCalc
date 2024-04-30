import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mariuszgromada.math.mxparser.Expression

class CalculatorViewModel : ViewModel() {

    private val _input = MutableLiveData("")
    val input: LiveData<String> = _input

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    // Add a LiveData for tracking the state of scientific notation
    private val _showScientificNotation = MutableLiveData<Boolean>(false)
    val showScientificNotation: LiveData<Boolean> = _showScientificNotation

    fun onButtonClick(button: String) {
        _input.value = (_input.value ?: "") + button
    }

    fun onOperationClick(operation: String) {
        _input.value = formatExpression(_input.value ?: "") + " $operation "
    }

    fun onEqualsClick() {
        val expression = formatExpression(_input.value ?: "")
        try {
            val resultValue = evaluateExpression(expression)
            _result.value = if (_showScientificNotation.value == true) {
                resultValue.toString() // Return as string
            } else {
                String.format("%.5f", resultValue) // Format to 5 decimal places
            }
        } catch (e: Exception) {
            _result.value = "Error: ${e.message}"
        }
    }

    fun onClearClick() {
        _input.value = ""
        _result.value = ""
    }

    // Modify toggleScientificNotation to toggle the state
    fun toggleScientificNotation(isChecked: Boolean) {
        _showScientificNotation.value = isChecked
    }

    private fun formatExpression(expression: String): String {
        var formattedExpression = expression.replace("+", " + ")
            .replace("-", " - ")
            .replace("*", " * ")
            .replace("/", " / ")

        formattedExpression = formattedExpression.trim()

        return formattedExpression
    }

    private fun evaluateExpression(expression: String): Double {
        val e = Expression(expression)
        return e.calculate()
    }
}
