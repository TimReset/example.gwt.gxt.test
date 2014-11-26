package ru.timreset.example.test.base;

import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.WithTokenizers;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Реализация {@link PlaceHistoryMapper} для тестирования. Использует GWT маппинг из {@link ru.timreset.example.gxt.client.PlaceHistoryMapper}.
 */
public class PlaceHistoryMapper extends AbstractPlaceHistoryMapper {

    private Map<Class<? extends Place>, PlaceTokenizer<?>> placeToTokenizer = new HashMap<Class<? extends Place>, PlaceTokenizer<?>>();

    public PlaceHistoryMapper() throws IllegalAccessException, InstantiationException {
        WithTokenizers withTokenizers = ru.timreset.example.gxt.client.PlaceHistoryMapper.class.getAnnotation(WithTokenizers.class);
        Class<? extends PlaceTokenizer<?>>[] tokenizerClasses = withTokenizers.value();
        for (Class<? extends PlaceTokenizer<?>> t : tokenizerClasses) {
            placeToTokenizer.put(getPlaceByTokenizer(t), t.newInstance());
        }
    }

    private static Class<? extends Place> getPlaceByTokenizer(Class<? extends PlaceTokenizer> tokenizerClass) {
        Type[] interfaces = tokenizerClass.getGenericInterfaces();
        for (Type t : interfaces) {
            if (t instanceof ParameterizedType && PlaceTokenizer.class.equals(((ParameterizedType) t).getRawType())) {
                return ((Class) ((ParameterizedType) t).getActualTypeArguments()[0]);
            }
        }
        throw new IllegalArgumentException(tokenizerClass.getName() + " is not implement " + PlaceTokenizer.class.getName());
    }

    @Override
    @Nullable
    protected PlaceTokenizer<?> getTokenizer(String initial) {
        for (Map.Entry<Class<? extends Place>, PlaceTokenizer<?>> entry : placeToTokenizer.entrySet()) {
            if (initial.equals(entry.getKey().getSimpleName())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Nullable
    @Override
    protected AbstractPlaceHistoryMapper.PrefixAndToken getPrefixAndToken(Place newPlace) {
        PlaceTokenizer tokenizer = placeToTokenizer.get(newPlace.getClass());
        if (tokenizer == null) {
            return null;
        } else {
            return new PrefixAndToken(newPlace.getClass().getSimpleName(), tokenizer.getToken(newPlace));
        }
    }
}
