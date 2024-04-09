"use client";

import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Link,
  Button,
  Select,
  Input,
  SelectItem,
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  useDisclosure,
  Avatar,
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
  Chip,
} from "@nextui-org/react";

import { Currency, currencyCodes } from "@/types/currency";
import { useCookies } from "next-client-cookies";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { UserCreate } from "@/types/user";
import { createUser, loginUser } from "@/service/userService";
import { MaterialSymbol } from "react-material-symbols";

export default function NavbarDesign() {
  const router = useRouter();
  const cookies = useCookies();

  const [currency, setCurrency] = useState<Currency>(
    (cookies.get("currency") as Currency) || "EUR"
  );

  useEffect(() => {
    if (cookies.get("currency") === undefined) {
      cookies.set("currency", "EUR");
    }
  }, [cookies]);

  const [user, setUser] = useState<UserCreate>({
    username: "",
    name: "",
    email: "",
    password: "",
  });

  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [mode, setMode] = useState<"login" | "signup">("login");
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(
    cookies.get("user") !== undefined
  );
  const [loginError, setLoginError] = useState<boolean>(false);

  return (
    <>
      <Navbar>
        <NavbarBrand>
          <Link
            color="foreground"
            href="/"
            id="homeBtn"
            onClick={() => {
              cookies.remove("trip");
              cookies.remove("seats");
              cookies.remove("reservation");
              cookies.remove("departure");
              cookies.remove("arrival");
              router.push("/");
            }}
          >
            <p className="text-xl font-bold text-inherit">TripFinder</p>
          </Link>
        </NavbarBrand>
        <NavbarContent justify="end">
          {isLoggedIn ? (
            <Dropdown>
              <DropdownTrigger>
                <Avatar fallback={<MaterialSymbol icon="person" size={24} />} />
              </DropdownTrigger>
              <DropdownMenu aria-label="Static Actions">
                <DropdownItem
                  key="edit"
                  id="myReservationsBtn"
                  onClick={() => {
                    router.push("/reservation/my");
                  }}
                >
                  My reservations
                </DropdownItem>
                <DropdownItem
                  key="delete"
                  className="text-danger"
                  id="logoutBtn"
                  color="danger"
                  onClick={() => {
                    cookies.remove("user");
                    setIsLoggedIn(false);
                  }}
                >
                  Logout
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          ) : (
            <>
              <NavbarItem className="hidden lg:flex">
                <Button
                  variant="light"
                  color="primary"
                  id="loginBtn"
                  onPress={() => {
                    setMode("login");
                    setLoginError(false);
                    onOpen();
                  }}
                >
                  Login
                </Button>
              </NavbarItem>
              <NavbarItem>
                <Button
                  color="primary"
                  variant="flat"
                  id="signUpBtn"
                  onClick={() => {
                    setMode("signup");
                    setLoginError(false);
                    onOpen();
                  }}
                >
                  Sign Up
                </Button>
              </NavbarItem>
            </>
          )}
          <NavbarItem>
            <Select
              label="Currency"
              size="sm"
              variant="flat"
              id="currencySelect"
              className="w-28"
              selectedKeys={[currency]}
              onChange={(event) => {
                const currency = event.target.value as Currency;
                setCurrency(currency);
                cookies.set("currency", currency);
              }}
            >
              {Object.values(currencyCodes).map((currency) => (
                <SelectItem key={currency} value={currency} id={"currency" + currency}>
                  {currency}
                </SelectItem>
              ))}
            </Select>
          </NavbarItem>
        </NavbarContent>
      </Navbar>

      <Modal isOpen={isOpen} onOpenChange={onOpenChange} backdrop="blur">
        <ModalContent>
          {(onClose) => (
            <ModalContent>
              <ModalHeader className="flex flex-col gap-1">
                Let&apos;s get signed {mode == "login" ? "in" : "up"}.
              </ModalHeader>
              <ModalBody>
                {mode === "signup" && (
                  <>
                    <Input
                      label="Username"
                      id="username"
                      onValueChange={(value) =>
                        setUser((previous) => ({
                          ...previous,
                          username: value,
                        }))
                      }
                    />
                    <Input
                      label="Name"
                      id="name"
                      onValueChange={(value) =>
                        setUser((previous) => ({
                          ...previous,
                          name: value,
                        }))
                      }
                    />
                  </>
                )}
                <Input
                  label="Email"
                  type="email"
                  id="email"
                  onValueChange={(value) =>
                    setUser((previous) => ({ ...previous, email: value }))
                  }
                />
                <Input
                  label="Password"
                  type="password"
                  id="password"
                  onValueChange={(value) =>
                    setUser((previous) => ({ ...previous, password: value }))
                  }
                />
                {mode === "login" && loginError && (
                  <Chip color="danger" className="self-center">
                    Wrong login information.
                  </Chip>
                )}
              </ModalBody>
              <ModalFooter>
                <Button
                  color="danger"
                  variant="light"
                  onClick={onClose}
                  id="closeBtn"
                >
                  Close
                </Button>
                <Button
                  color="primary"
                  id="submitBtn"
                  onClick={async () => {
                    if (mode === "login") {
                      const loggedInUser = await loginUser({
                        email: user.email,
                        password: user.password,
                      });
                      console.log(loggedInUser);
                      if (loggedInUser === null) {
                        setLoginError(true);
                        return;
                      }
                      cookies.set("user", JSON.stringify(loggedInUser));
                      setIsLoggedIn(true);
                    } else {
                      const createdUser = await createUser(user);
                      cookies.set("user", JSON.stringify(createdUser));
                      setIsLoggedIn(true);
                    }
                    onClose();
                  }}
                >
                  {mode === "login" ? "Login" : "Sign up"}
                </Button>
              </ModalFooter>
            </ModalContent>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
