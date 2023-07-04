package com.zhuooo.jdbc;

import com.zhuooo.jdbc.annotations.*;
import com.zhuooo.jdbc.dao.BaseDaoMappedItem;
import com.zhuooo.jdbc.dao.BaseDaoStatement;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Component
public class DoScanner implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    public void setEnvironment(Environment environment) {
        this.environment = environment;

    }


    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // 创建scanner
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(Table.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        Set<String> basePackages = getBasePackages(metadata);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                loadStatement(candidateComponent.getBeanClassName());
            }
        }
    }


    /**
     * 创建扫描器
     */
    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        return basePackages;
    }

    private void loadStatement(String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            Table t = (Table) clazz.getAnnotation(Table.class);
            String tableName = t.value();
            BaseDaoStatement baseMapperStatement = new BaseDaoStatement();
            baseMapperStatement.setClassName(clazzName);
            baseMapperStatement.setClazz(clazz);
            baseMapperStatement.setTableName(tableName);
            loadStatement(clazz, baseMapperStatement);
            JdbcCache.put(baseMapperStatement.getClassName(), baseMapperStatement);
        } catch (Exception e) {
            throw new RuntimeException("init clazzName sql error!");
        }
    }

    private void loadStatement(Class clazz, BaseDaoStatement baseMapperStatement) {
        collectFields(clazz, baseMapperStatement);

        initInsertSql(baseMapperStatement);
        initIdSql(baseMapperStatement);
        initGroupSql(baseMapperStatement);
        initSelectSql(baseMapperStatement);
//        initSoftDeleteOneSql(baseMapperStatement);

    }

    private void collectFields(Class clazz, BaseDaoStatement statement) {
        if (clazz == null) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.getAnnotation(ID.class) != null) {
                statement.setIdMappedItem(new BaseDaoMappedItem(field.getName(), humpToLine(field.getName()), field));
            }

            if (field.getAnnotation(GroupId.class) != null) {
                statement.setGroupMappedItem(new BaseDaoMappedItem(field.getName(), humpToLine(field.getName()), field));
            }

            if (field.getAnnotation(Deleted.class) != null) {
                statement.setDeletedMappedItem(new BaseDaoMappedItem(field.getName(), humpToLine(field.getName()), field));
            }

            if (field.getAnnotation(Parent.class) != null) {
                statement.setParentMapperItem(new BaseDaoMappedItem(field.getName(), humpToLine(field.getName()), field));
            }

            if (field.getAnnotation(Column.class) != null) {
                statement.addMappedItem(new BaseDaoMappedItem(field.getName(), humpToLine(field.getName()), field));
            }

        }

        collectFields(clazz.getSuperclass(), statement);
    }


    private void initIdSql(BaseDaoStatement statement) {
        if (statement.getIdMappedItem() != null) {
            // selectOne
            {
                StringBuilder sb = new StringBuilder("select * from ").append(statement.getTableName()).append(" where ");
                if (statement.getDeletedMappedItem() != null) {
                    sb.append(statement.getDeletedMappedItem().getColumnName()).append(" = 0 and ");
                }
                sb.append(statement.getIdMappedItem().getColumnName()).append(" = ? ");
                statement.setSelectOneSql(sb.toString());
            }


            // delete
            {
                StringBuilder sb = new StringBuilder("delete from ").append(statement.getTableName()).append(" where ");
                sb.append(statement.getIdMappedItem().getColumnName()).append(" = ? ");
                statement.setDeleteOneSql(sb.toString());
            }

        }
    }

    private void initGroupSql(BaseDaoStatement statement) {
        if (statement.getGroupMappedItem() != null) {
            // select group
            {
                StringBuilder sb = new StringBuilder("select * from ").append(statement.getTableName()).append(" where ");
                sb.append(statement.getGroupMappedItem().getColumnName()).append(" = ?");
                statement.setSelectGroupSql(sb.toString());
            }

            // delete group
            {
                StringBuilder sb = new StringBuilder("delete from ").append(statement.getTableName()).append(" where ");
                sb.append(statement.getGroupMappedItem().getColumnName()).append(" = ?");
                statement.setDeleteGroupSql(sb.toString());
            }
        }
    }

    private void initSelectSql(BaseDaoStatement statement) {
        // select all
        {
            StringBuilder sb = new StringBuilder("select * from ");
            sb.append(statement.getTableName());
            if (statement.getDeletedMappedItem() != null) {
                sb.append(" where ").append(statement.getDeletedMappedItem().getColumnName()).append(" = 0");
            }
            statement.setSelectAllSql(sb.toString());
        }

        // select parent
        {
            if (statement.getParentMapperItem() != null) {
                StringBuilder sb = new StringBuilder("select * from ");
                sb.append(statement.getTableName()).append(" where ");
                if (statement.getDeletedMappedItem() != null) {
                    sb.append(statement.getDeletedMappedItem().getColumnName()).append(" = 0").append(" and ");
                }
                sb.append(statement.getParentMapperItem().getColumnName()).append(" = ?");
                statement.setSelectChildrenSql(sb.toString());
            }
        }


        statement.setSelectAllSql("select * from " + statement.getTableName());
    }


    private void initSoftDeleteOneSql(Class clazz, BaseDaoStatement baseMapperStatement) {
        if (baseMapperStatement.getIdMappedItem() != null) {
            StringBuilder sb = new StringBuilder("update ").append(baseMapperStatement.getTableName()).append(" set ");
            sb.append(" is_delete = 1 ").append(" where ").append(baseMapperStatement.getIdMappedItem().getColumnName()).append(" = ?");
            baseMapperStatement.setSoftDeleteOneSql(sb.toString());
        }
    }

    private void initInsertSql(BaseDaoStatement statement) {
        StringBuilder sb = new StringBuilder("insert into ");
        StringBuilder placeholder = new StringBuilder();

        sb.append(statement.getTableName()).append("(");

        for (BaseDaoMappedItem item : statement.getMappedItems()) {
            sb.append(item.getColumnName()).append(',');
            placeholder.append("?,");
        }

        placeholder.deleteCharAt(placeholder.length() - 1);
        placeholder.append(")");

        sb.deleteCharAt(sb.length() - 1);
        sb.append(") values (").append(placeholder.toString());

        statement.setInsertSql(sb.toString());
    }

    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z0-9]");

    //驼峰转下划线
    private String humpToLine(String str) {
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}
