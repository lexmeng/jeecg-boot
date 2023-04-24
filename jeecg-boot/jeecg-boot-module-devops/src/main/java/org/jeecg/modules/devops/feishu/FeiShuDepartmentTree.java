package org.jeecg.modules.devops.feishu;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.core.utils.Sets;
import com.lark.oapi.service.contact.v3.enums.ChildrenDepartmentDepartmentIdTypeEnum;
import com.lark.oapi.service.contact.v3.enums.ChildrenDepartmentUserIdTypeEnum;
import com.lark.oapi.service.contact.v3.enums.FindByDepartmentUserDepartmentIdTypeEnum;
import com.lark.oapi.service.contact.v3.enums.FindByDepartmentUserUserIdTypeEnum;
import com.lark.oapi.service.contact.v3.model.ChildrenDepartmentReq;
import com.lark.oapi.service.contact.v3.model.ChildrenDepartmentResp;
import com.lark.oapi.service.contact.v3.model.ChildrenDepartmentRespBody;
import com.lark.oapi.service.contact.v3.model.Department;
import com.lark.oapi.service.contact.v3.model.DepartmentStatus;
import com.lark.oapi.service.contact.v3.model.FindByDepartmentUserReq;
import com.lark.oapi.service.contact.v3.model.FindByDepartmentUserResp;
import com.lark.oapi.service.contact.v3.model.FindByDepartmentUserRespBody;
import com.lark.oapi.service.contact.v3.model.User;
import com.lark.oapi.service.tenant.v2.model.Tenant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Getter
public class FeiShuDepartmentTree implements Serializable {

    public static final String ROOT_DEPARTMENT_ID = "0";

    public static class FeiShuUserWrapper {

        private final User user;

        public FeiShuUserWrapper(User user) {
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FeiShuUserWrapper that = (FeiShuUserWrapper) o;
            return Objects.equals(this.user.getUserId(), that.user.getUserId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.user.getUserId());
        }

        public User get() {
            return this.user;
        }
    }

    @Getter
    public static class DepartmentNode implements Serializable {

        private final Department depart;

        private final List<DepartmentNode> childNodes = new ArrayList<>();

        private final List<User> users = new ArrayList<>();

        public DepartmentNode(Department depart) {
            this.depart = depart;
        }

        public static List<Department> recursionGetAllDepartments(DepartmentNode parent) {
            final List<Department> allDepartments = Lists.newArrayList(parent.getDepart());

            if (parent.getChildNodes().isEmpty()) {
                return allDepartments;
            }

            for (final DepartmentNode child : parent.getChildNodes()) {
                allDepartments.addAll(recursionGetAllDepartments(child));
            }

            return allDepartments;
        }

        public static Set<FeiShuUserWrapper> recursionGetAllUsers(DepartmentNode depart) {
            final Set<FeiShuUserWrapper> allUsers = depart.getUsers().stream().map(FeiShuUserWrapper::new).collect(Collectors.toSet());

            if (depart.getChildNodes().isEmpty()) {
                return allUsers;
            }

            for (final DepartmentNode child : depart.getChildNodes()) {
                allUsers.addAll(recursionGetAllUsers(child));
            }

            return allUsers;
        }
    }

    private Tenant tenant;

    private User admin;

    private List<DepartmentNode> departmentNodes;

    public static volatile FeiShuDepartmentTree INSTANCE;

    public static FeiShuDepartmentTree getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (FeiShuDepartmentTree.class) {
                if (INSTANCE == null) {
                    INSTANCE = FeiShuDepartmentTree.build();
                }
            }
        }

        return INSTANCE;
    }


    public static FeiShuDepartmentTree build() throws Exception {
        log.debug("FeiShuDepartmentTree building...");
        final FeiShuDepartmentTree tree = new FeiShuDepartmentTree();

        final Client client = FeiShuUtils.getClient();

        tree.tenant = FeiShuUtils.getRespBody(client.tenant().tenant().query()).getTenant();
        log.debug("FeiShuDepartmentTree.tenant = {}", Jsons.createGSON(true, false).toJson(tree.tenant));
        tree.admin = getDepartmentUsers(client, ROOT_DEPARTMENT_ID, tree.tenant.getName()).get(0);
        log.debug("FeiShuDepartmentTree.admin = {}", tree.admin.getName());
        tree.departmentNodes = getChildrenDepartments(client, ROOT_DEPARTMENT_ID, tree.tenant.getName()).stream().map(DepartmentNode::new).collect(Collectors.toList());

        final Queue<DepartmentNode> queue = new ArrayDeque<>(tree.departmentNodes);
        while (queue.peek() != null) {
            final DepartmentNode parent = queue.poll();
            final List<DepartmentNode> children = getChildrenDepartments(client, parent.depart.getDepartmentId(), parent.depart.getName()).stream().map(DepartmentNode::new).collect(Collectors.toList());
            parent.childNodes.addAll(children);
            parent.users.addAll(getDepartmentUsers(client, parent.depart.getDepartmentId(), parent.depart.getName()));
            queue.addAll(children);
        }

        log.debug("FeiShuDepartmentTree.departmentNodes.count = {}", tree.departmentNodes.size());

        return tree;
    }

    private static List<Department> getChildrenDepartments(final Client client, final String parentDepartmentId, final String parentDepartmentName) throws Exception {
        final List<Department> children = new ArrayList<>();

        String pageToken = null;
        ChildrenDepartmentRespBody body = null;

        while ((pageToken == null && body == null) || body.getHasMore()) {
            final ChildrenDepartmentResp resp;
            resp = client.contact().department().children(ChildrenDepartmentReq.newBuilder()
                    .departmentId(parentDepartmentId)
                    .pageSize(50)
                    .pageToken(pageToken)
                    .userIdType(ChildrenDepartmentUserIdTypeEnum.USER_ID)
                    .departmentIdType(ChildrenDepartmentDepartmentIdTypeEnum.DEPARTMENT_ID)
                    .build());

            body = FeiShuUtils.getRespBody(resp);

            final Department[] items = FeiShuUtils.getRespBody(resp).getItems();
            if (items != null) {
                children.addAll(Arrays.asList(items));
            }

            pageToken = body.getPageToken();
        }

        log.debug("[{}].childrenDepartments = {}", parentDepartmentName, Jsons.createGSON(true, false).toJson(children.stream().map(Department::getName).collect(Collectors.toList())));
        return children;
    }

    private static List<User> getDepartmentUsers(final Client client, final String departmentId, final String departmentName) throws Exception {
        final List<User> users = Lists.newArrayList();

        String pageToken = null;
        FindByDepartmentUserRespBody body = null;

        while ((pageToken == null && body == null) || body.getHasMore()) {
            final FindByDepartmentUserResp resp;
            resp = client.contact().user().findByDepartment(FindByDepartmentUserReq.newBuilder()
                    .departmentId(departmentId)
                    .pageSize(50)
                    .userIdType(FindByDepartmentUserUserIdTypeEnum.USER_ID)
                    .departmentIdType(FindByDepartmentUserDepartmentIdTypeEnum.DEPARTMENT_ID)
                    .build());


            body = FeiShuUtils.getRespBody(resp);

            if (body.getItems() != null) {
                users.addAll(Arrays.asList(body.getItems()));
            }

            pageToken = body.getPageToken();
        }

        log.debug("{}.users.count = {}", departmentName, users.size());

        return users;
    }

    public List<Department> getAllDepartment() {
        final List<Department> r = Lists.newArrayList(Iterators.singletonIterator(getTenantAsDepartment()));
        r.addAll(this.departmentNodes.stream().map(DepartmentNode::recursionGetAllDepartments).flatMap(List::stream).collect(Collectors.toList()));
        log.debug("FeiShuDepartmentTree.allDepartments.count = {}", r.size());
        return r;
    }

    public Set<FeiShuUserWrapper> getAllUsers() {
        final Set<FeiShuUserWrapper> r = Sets.newHashSet(new FeiShuUserWrapper(this.admin));
        r.addAll(this.departmentNodes.stream().map(DepartmentNode::recursionGetAllUsers).flatMap(Set::stream).collect(Collectors.toSet()));
        log.debug("FeiShuDepartmentTree.allUsers.count = {}", r.size());
        return r;
    }

    public Department getTenantAsDepartment() {
        return Department.newBuilder()
                .departmentId(ROOT_DEPARTMENT_ID)
                .name(this.tenant.getName())
                .order("0")
                .status(DepartmentStatus.newBuilder().isDeleted(false).build())
                .build();
    }
}
