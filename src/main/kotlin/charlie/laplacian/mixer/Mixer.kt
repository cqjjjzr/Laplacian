package charlie.laplacian.mixer

interface Mixer {
    fun init(sampleRateHz: Float, bitDepth: Int, numChannel: Int)

    fun openChannel(): AudioChannel
    fun closeChannel(channel: AudioChannel)
}