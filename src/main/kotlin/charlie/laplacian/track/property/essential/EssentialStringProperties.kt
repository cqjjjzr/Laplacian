package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyType

abstract class StringTypeProperty(private val value: String, private val i18NKey: String): Property {
    override fun getName(): String = I18n.getString(i18NKey)

    override fun getValue(): Any = value

    override fun getType(): PropertyType = PropertyType.STRING
}

class NameProperty(value: String): StringTypeProperty(value, "property.essential.nameProperty.name")
class ArtistProperty(value: String): StringTypeProperty(value, "property.essential.artistProperty.name")
class AlbumProperty(value: String): StringTypeProperty(value, "property.essential.albumProperty.name")
class AlbumArtistProperty(value: String): StringTypeProperty(value, "property.essential.albumArtistProperty.name")
class ComposerProperty(value: String): StringTypeProperty(value, "property.essential.composerProperty.name")
class GenreProperty(value: String): StringTypeProperty(value, "property.essential.genreProperty.name")