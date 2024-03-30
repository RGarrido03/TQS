export type User = {
  id: number;
  username: string;
  name: string;
  email: string;
};

export type UserCreate = Omit<User, "id"> & {
  password: string;
};

export type UserReference = Pick<User, "id">;
