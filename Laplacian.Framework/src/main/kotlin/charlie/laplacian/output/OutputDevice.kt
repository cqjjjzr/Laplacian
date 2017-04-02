package charlie.laplacian.output

interface OutputMethod {
    fun init()
    fun destroy()

    fun openDevice(outputSettings: OutputSettings): OutputDevice

    fun getMetadata(): OutputMethodMetadata
    fun getDeviceInfos(): Array<out OutputDeviceInfo>
}

interface OutputMethodMetadata {
    fun getName(): String

    fun getVersion(): String
    fun getVersionID(): Int
}

interface OutputDevice {
    fun openLine(): OutputLine
    fun closeLine(channel: OutputLine)
}

interface OutputDeviceInfo {
    fun getName(): String
    fun getAvailableSettings(): Array<OutputSettings>
}