package charlie.laplacian.track.property.essential

import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_ONLY
import java.util.*

class EqualsProperty(value: Array<UUID>)
    : I18nProperty<Array<UUID>>(value, "property.essential.equalsProperty.name", TRACK_ONLY, false)