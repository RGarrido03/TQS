"use client";

import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Link,
  Button,
  Select,
  SelectItem,
} from "@nextui-org/react";

import { Currency, currencyCodes } from "@/types/currency";
import { useCookies } from "next-client-cookies";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

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

  return (
    <Navbar>
      <NavbarBrand>
        <Link
          color="foreground"
          href="/"
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
        <NavbarItem className="hidden lg:flex">
          <Link href="#">Login</Link>
        </NavbarItem>
        <NavbarItem>
          <Button as={Link} color="primary" href="#" variant="flat">
            Sign Up
          </Button>
        </NavbarItem>
        <NavbarItem>
          <Select
            label="Currency"
            size="sm"
            variant="flat"
            className="w-28"
            selectedKeys={[currency]}
            onChange={(event) => {
              const currency = event.target.value as Currency;
              setCurrency(currency);
              cookies.set("currency", currency);
            }}
          >
            {Object.values(currencyCodes).map((currency) => (
              <SelectItem key={currency} value={currency}>
                {currency}
              </SelectItem>
            ))}
          </Select>
        </NavbarItem>
      </NavbarContent>
    </Navbar>
  );
}
