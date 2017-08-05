package charlie.laplacian.track.property.essential

import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_AND_GROUPING
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_ONLY

class TrackNumberProperty(value: Int): I18nProperty<Int>(value, "property.essential.trackNumberProperty.name",
        TRACK_ONLY)
class ReleaseYearProperty(value: Int): I18nProperty<Int>(value, "property.essential.releaseYearProperty.name",
        TRACK_AND_GROUPING)
class BPMProperty(value: Int): I18nProperty<Int>(value, "property.essential.bpmProperty.name",
        TRACK_ONLY)