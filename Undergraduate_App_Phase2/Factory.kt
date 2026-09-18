class Factory(
    private val repository: DegreeRepo
) : ViewModelProvider.Factory {

    override fun < T : ViewModel > create (
        modelClass: Class<T>
    ) : T {
        if (modelClass.isAssignableFrom(DegreeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DegreeViewModel(repository) as T
        }
        throw IllegalArgumentException("I don't recognize your viewmodel class!")
    }
}