import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssemblyLine implements IAssemblyLine {
    private List<ILineStep> steps = new ArrayList<>();

    public AssemblyLine(ILineStep... steps) {
        this.steps.addAll(Arrays.asList(steps));
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        List<IProductPart> parts = new ArrayList<>();
        for (ILineStep step : steps) {
            parts.add(step.buildProductPart());
        }
        product.installFirstPart(parts.get(0));
        product.installSecondPart(parts.get(1));
        product.installThirdPart(parts.get(2));
        return product;
    }
}
