package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.PropertyApplicableType
import charlie.laplacian.track.property.PropertyImpl

abstract class I18nProperty<out T>(propValue: T,
                                            propDisplayNameI18nKey: String,
                                            applicableTypes: Set<PropertyApplicableType>,
                                            isPropStringConvertible: Boolean = true)
    : PropertyImpl<T>(propValue, I18n.getString(propDisplayNameI18nKey), applicableTypes, isPropStringConvertible)