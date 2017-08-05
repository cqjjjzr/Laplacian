package charlie.laplacian.track.property.essential

import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_AND_GROUPING
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_ONLY

class NameProperty(value: String): I18nProperty<String>(value, "property.essential.nameProperty.name",
        TRACK_AND_GROUPING)
class ArtistProperty(value: Array<String>): I18nProperty<Array<String>>(value, "property.essential.artistProperty.name",
        TRACK_AND_GROUPING) {
    constructor(valueSingle: String): this(Array(1, { valueSingle }))
}
class AlbumProperty(value: String): I18nProperty<String>(value, "property.essential.albumProperty.name",
        TRACK_ONLY)
class AlbumArtistProperty(value: Array<String>): I18nProperty<Array<String>>(value, "property.essential.albumArtistProperty.name",
        TRACK_ONLY) {
    constructor(valueSingle: String): this(Array(1, { valueSingle }))
}
class ComposerProperty(value: Array<String>): I18nProperty<Array<String>>(value, "property.essential.composerProperty.name",
        TRACK_AND_GROUPING) {
    constructor(valueSingle: String): this(Array(1, { valueSingle }))
}
class GenreProperty(value: String): I18nProperty<String>(value, "property.essential.genreProperty.name",
        TRACK_AND_GROUPING)