package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_AND_GROUPING
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_ONLY
import charlie.laplacian.track.property.PropertyType

abstract class StringTypeProperty(private var value: String,
                                  private val i18NKey: String,
                                  private val applicableTypes: Set<PropertyApplicableType>): Property() {
    override fun getName(): String = I18n.getString(i18NKey)

    override fun getValue(): Any = value

    override fun getType(): PropertyType = PropertyType.STRING

    override fun setValue(value: Any) {
        if (value !is String) throw ClassCastException()
        this.value = value
    }

    override fun getApplicableFor(): Set<PropertyApplicableType> = applicableTypes
}

class NameProperty(value: String): StringTypeProperty(value, "property.essential.nameProperty.name",
        TRACK_AND_GROUPING)
class ArtistProperty(value: String): StringTypeProperty(value, "property.essential.artistProperty.name",
        TRACK_AND_GROUPING)
class AlbumProperty(value: String): StringTypeProperty(value, "property.essential.albumProperty.name",
        TRACK_ONLY)
class AlbumArtistProperty(value: String): StringTypeProperty(value, "property.essential.albumArtistProperty.name",
        TRACK_ONLY)
class ComposerProperty(value: String): StringTypeProperty(value, "property.essential.composerProperty.name",
        TRACK_AND_GROUPING)
class GenreProperty(value: String): StringTypeProperty(value, "property.essential.genreProperty.name",
        TRACK_AND_GROUPING)