package brainflow.application.presentation;

import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Sep 1, 2007
 * Time: 5:01:27 PM
 */
public class BindingUtils {

    public static BoundedRangeAdapter createPercentageBasedRangeModel(ValueModel value, double min, double max, int numUnits) {

        PercentageConverter converter = new PercentageConverter(value, new ValueHolder(min), new ValueHolder(max), numUnits);
        BoundedRangeAdapter adapter = new BoundedRangeAdapter(converter, 0, 0, numUnits);

        return adapter;
    }

    public static BoundedRangeAdapter createPercentageBasedRangeModel(ValueModel value, ValueModel min, ValueModel max, int numUnits) {

        PercentageConverter converter = new PercentageConverter(value, min, max, numUnits);
        BoundedRangeAdapter adapter = new BoundedRangeAdapter(converter, 0, 0, numUnits);

        return adapter;
    }

}
