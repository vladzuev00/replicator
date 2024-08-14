package by.aurorasoft.replicator.annotation.processor;

@AutoService(Processor.class)
public final class ReplicatedServiceProcessor extends AbstractProcessor {

    @SuppressWarnings("deprecation")
    private static final Set<String> RUD_SERVICE_NAMES = getNames(AbsServiceRUD.class, RudGenericService.class);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        annotations.stream()
                .flatMap(annotation -> env.getElementsAnnotatedWith(annotation).stream())
                .filter(element -> !isRUDService(element))
                .forEach(this::alertWrongAnnotating);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ReplicatedService.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    private static Set<String> getNames(Class<?>... classes) {
        return stream(classes)
                .map(Class::getName)
                .collect(toCollection(LinkedHashSet::new));
    }

    private boolean isRUDService(Element element) {
        return RUD_SERVICE_NAMES.stream()
                .map(this::getRawType)
                .anyMatch(rawServiceType -> isInstanceOf(element, rawServiceType));
    }

    private TypeMirror getRawType(String name) {
        return getTypeUtils().erasure(getType(name));
    }

    private TypeMirror getType(String name) {
        return getElementUtils()
                .getTypeElement(name)
                .asType();
    }

    private boolean isInstanceOf(Element element, TypeMirror type) {
        return getTypeUtils().isAssignable(element.asType(), type);
    }

    private void alertWrongAnnotating(Element element) {
        getMessager().printMessage(ERROR, getWrongAnnotatingMessage(), element);
    }

    private String getWrongAnnotatingMessage() {
        return "'@%s' can be applied only for subclass one of '%s'".formatted(
                ReplicatedService.class.getName(),
                RUD_SERVICE_NAMES
        );
    }

    private Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

    private Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    private Messager getMessager() {
        return processingEnv.getMessager();
    }
}
