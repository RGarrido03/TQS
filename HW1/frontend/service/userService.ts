import { User, UserCreate, UserLogin } from "@/types/user";
import { BASE_API_URL } from "./config";
import { Reservation } from "@/types/reservation";

export const createUser = async (user: UserCreate): Promise<User> =>
  fetch(BASE_API_URL + "user", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  }).then((res) => res.json());

export const loginUser = async (user: UserLogin): Promise<User> =>
  fetch(BASE_API_URL + "user/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  }).then((res) => {
    if (res.status === 401) {
      return null;
    }
    return res.json();
  });

export const getUser = async (id: number): Promise<User> =>
  fetch(BASE_API_URL + "user/" + id, {
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());

export const getUserReservations = async (id: number): Promise<Reservation[]> =>
  fetch(BASE_API_URL + "user/" + id + "/reservations", {
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());

export const updateUser = async (id: number, user: UserCreate): Promise<User> =>
  fetch(BASE_API_URL + "user/" + id, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  }).then((res) => res.json());

export const deleteUser = async (id: number): Promise<any> =>
  fetch(BASE_API_URL + "user/" + id, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());
