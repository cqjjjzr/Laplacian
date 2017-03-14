package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.*

abstract class StringTypeProperty(private var value: String,
                                  private val i18NKey: String,
                                  private val applicableTypes: Set<PropertyApplicableType>,
                                  private val canDuplicate: Boolean): Property() {
    override fun getName(): String = I18n.getString(i18NKey)

    override fun getValue(): Any = value

    override fun getType(): PropertyType = PropertyType.STRING

    override fun setValue(value: Any) {
        if (value !is String) throw ClassCastException()
        this.value = value
    }

    override fun getApplicableFor(): Set<PropertyApplicableType> = applicableTypes

    override fun canDuplicate(): Boolean = canDuplicate
}

class NameProperty(value: String): StringTypeProperty(value, "property.essential.nameProperty.name",
        PROPERTY_APPLICABLE_TRACK_AND_GROUPING, false)
class ArtistProperty(value: String): StringTypeProperty(value, "property.essential.artistProperty.name",
        PROPERTY_APPLICABLE_TRACK_AND_GROUPING, true)
class AlbumProperty(value: String): StringTypeProperty(value, "property.essential.albumProperty.name",
        PROPERTY_APPLICABLE_TRACK_ONLY, false)
class AlbumArtistProperty(value: String): StringTypeProperty(value, "property.essential.albumArtistProperty.name",
        PROPERTY_APPLICABLE_TRACK_ONLY, false)
class ComposerProperty(value: String): StringTypeProperty(value, "property.essential.composerProperty.name",
        PROPERTY_APPLICABLE_TRACK_AND_GROUPING, false)
class GenreProperty(value: String): StringTypeProperty(value, "property.essential.genreProperty.name",
        PROPERTY_APPLICABLE_TRACK_AND_GROUPING, true)