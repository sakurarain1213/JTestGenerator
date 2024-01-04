package jtg.generator;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.api.RandomizerRegistry;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomUtils.nextDouble;

/**
 * 随机工具，封装 EasyRandom 提供的对象和 List 的随机生成方法

 */
public class RandomUtil {
    private static final EasyRandom easyRandom;
    private RandomUtil() {
        throw new UnsupportedOperationException("静态工具类不允许被实例化");
    }

    static {
        EasyRandomParameters param = new EasyRandomParameters();
        param.setStringLengthRange(new EasyRandomParameters.Range<>(5, 10));
        // 注册自定义随机生成器

        param.randomizerRegistry(new BigDecimalRegistry());
        // 生成的对象是一个是接口或者抽象类，则扫描类路径找到它的一个具体实现类
        //设置
//        parameters.stringLengthRange(3, 3);
//        parameters.collectionSizeRange(5, 5);
//        parameters.excludeField(FieldPredicates.named("lastName").and(FieldPredicates.inClass(Employee.class)));
//        parameters.excludeType(TypePredicates.inPackage("not.existing.pkg"));
//        parameters.randomize(YearQuarter.class, new YearQuarterRandomizer());
        param.setScanClasspathForConcreteTypes(true);
        param.setObjectPoolSize(100);
        param.setRandomizationDepth(12);
        easyRandom = new EasyRandom(param);
    }

    /* *
    * 根据给定的String类名生成一个随机对象
     */
    public static Object nextObject(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

            Class<?> clazz = Class.forName(className);
            Object obj=clazz.newInstance();
            return nextObject(obj.getClass());

    }

    /**
     * 根据给定的类型生成一个随机的对象
     */
    public static <T> T nextObject(Class<T> clz) {
        return easyRandom.nextObject(clz);
    }
    /**
     * 根据给定的类型和大小生成一个随机对象的列表
     */
    public static <T> List<T> nextList(Class<T> clz, int size) {
        return objects(clz, size).collect(Collectors.toList());
    }
    /**
     * 根据给定的类型和大小生成一个随机对象的集合
     */
    public static <T> Set<T> nextSet(Class<T> clz, int size) {
        return objects(clz, size).collect(Collectors.toSet());
    }
    /**
     * 根据给定的类型和大小生成一个随机对象流
     */
    public static <T> Stream<T> objects(Class<T> clz, int size) {
        return easyRandom.objects(clz, size);
    }
    /**
     * 默认生成的 BigDecimal 实例精度过大，将会导致用于插入数据库时超过精度而报错，所以我们默认精度取为5
     */
    private static class BigDecimalRegistry implements RandomizerRegistry {
        static final BigDecimalRandomizer bigDecimalRandomizer = new BigDecimalRandomizer();

        @Override
        public void init(EasyRandomParameters easyRandomParameters) { }
        @Override
        public Randomizer<?> getRandomizer(Field field) {
            if (field.getType() == BigDecimal.class) {
                return bigDecimalRandomizer;
            } else {
                return null;
            }
        }
        @Override
        public Randomizer<?> getRandomizer(Class<?> aClass) {
            if (aClass == BigDecimal.class) {
                return bigDecimalRandomizer;
            } else {
                return null;
            }
        }
    }
    private static class BigDecimalRandomizer implements Randomizer<BigDecimal> {
        @Override
        public BigDecimal getRandomValue() {
            return BigDecimal.valueOf(nextDouble(Integer.MIN_VALUE, Integer.MAX_VALUE))
                    .setScale(5, BigDecimal.ROUND_HALF_UP);
        }
    }
}
