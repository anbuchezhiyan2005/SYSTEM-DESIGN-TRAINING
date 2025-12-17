class User {
    private String name;
    private int age;
    private String email;
    private String address;

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.address = builder.address;
    }

    public static class Builder {
        private String name;
        private int age;
        private String email;
        private String address;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setAge(int age) {
            this.age = age;
            return this;
        }
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }
        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

public class BuilderPattern {
    public static void main(String args[]) {
        User user = new User.Builder()
                        .setName("Alan")
                        .setAge(21)
                        .setEmail("alan@example.com")
                        .setAddress("123 Main St")
                        .build();
    }
}
